package net.kaminari;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Protocol<PQ extends IProtocolQueues> implements IProtocol<PQ> {
	private class ResolvedBlock
    {
        public Byte loopCounter;
        public ArrayList<Byte> packetCounters;
        
        public ResolvedBlock(Byte loopCounter) {
        	this.loopCounter = loopCounter;
        	this.packetCounters = new ArrayList<Byte>();
        }
    };
    
	private Short bufferSize;
	private Short sinceLastPing;
	private Short sinceLastRecv;
	private Short lastBlockIdRead;
	private Short expectedBlockId;
	private Byte loopCounter;
	private long timestamp;
	private Short timestampBlockId;
	private Map<Short, ResolvedBlock> alreadyResolved;
	
	public Protocol() {
		Reset();
	}
	
	public Short getLastBlockIdRead() {
		return this.lastBlockIdRead;
	}
	
	public Short getExpectedBlockId() {
		return this.expectedBlockId;
	}
	
	public boolean isExpected(Short id) {
		return expectedBlockId == 0 || Overflow.le(id, expectedBlockId);
	}
	
	public void setTimestamp(long timestamp, Short blockId) {
		this.timestamp = timestamp;
		this.timestampBlockId = blockId;
	}
	
	public Long blockTimestamp(Short blockId) {
		if (Overflow.ge(blockId, timestampBlockId)) {
			return timestamp + (blockId - timestampBlockId) * Constants.WorldHeartBeat;
		}
		
		return timestamp - (timestampBlockId - blockId) * Constants.WorldHeartBeat;
	}
	
	public void Reset() {
		bufferSize = 0;
		sinceLastPing = 0;
		sinceLastRecv = 0;
		lastBlockIdRead = 0;
		expectedBlockId = 0;
		loopCounter = 0;
		timestamp = System.currentTimeMillis();
		timestampBlockId = 0;
		alreadyResolved = new HashMap<Short, ResolvedBlock>();
	}
	
	public Buffer update(IBaseClient client, SuperPacket<PQ> superpacket) {
		++sinceLastPing;
		
		// TODO(gpascualg): Lock superpacket
		if (superpacket.finish() || needsPing()) {
			if (needsPing())
			{
				sinceLastPing = 0;
			}
			
			return new Buffer(superpacket.getBuffer());
		}
		
		return null;
	}
	
	private boolean needsPing() {
		return sinceLastPing >= 20; 
	}

	public boolean read(IBaseClient client, SuperPacket<PQ> superpacket, IHandlePacket handler) {
		timestampBlockId = expectedBlockId;
		timestamp = System.currentTimeMillis();
		
		if (!client.hasPendingSuperPackets()) {
			expectedBlockId = (short)(expectedBlockId + 1);
			
			if (Unsigned.geq(++sinceLastRecv, Constants.MaxBlocksUntilDisconnection)) {
				client.disconnect();
			}
			
			return false;
		}
		
		sinceLastRecv = 0;
		short expectedId = Overflow.sub(expectedBlockId, bufferSize);
		
		while (client.hasPendingSuperPackets() &&
				!Overflow.geq(client.firstSuperPacketId(), expectedId)) {
			read_impl(client, superpacket, handler);
		}

		expectedBlockId = (short)(expectedBlockId + 1);
		return true;
	}

	public void read_impl(IBaseClient client, SuperPacket<PQ> superpacket, IHandlePacket handler) {
		SuperPacketReader<PQ> reader = new SuperPacketReader<PQ>(client.popPendingSuperPacket());
		
		if (Overflow.le(reader.id(), lastBlockIdRead)) {
			return;
		}
		
		if (Overflow.sub(expectedBlockId, reader.id()) > Constants.MaximumBlocksUntilResync) {
			// TODO(gpascualg): Flag resync
		}
		
		if (Unsigned.ge(lastBlockIdRead, reader.id())) {
			loopCounter = (byte)(loopCounter + 1);
		}
		
		lastBlockIdRead = reader.id();
		
		for (Short ack : reader.getAcks()) {
			superpacket.getQueues().ack(ack);
			// TODO(gpascualg): Lag compensation
		}
		
		if (reader.hasData() || reader.isPingPacket()) {
			superpacket.scheduleAck(lastBlockIdRead);
		}
		
		reader.handlePackets(this, handler, client);
	}
	
	public boolean resolve(PacketReader packet, Short blockId) {
		Byte id = packet.getId();
		
		if (alreadyResolved.containsKey(blockId)) {
			ResolvedBlock info = alreadyResolved.get(blockId);
			
			if (info.loopCounter != loopCounter) {
				if (Overflow.sub(lastBlockIdRead, blockId) > Constants.MaximumBlocksUntilResync) {
					info.loopCounter = loopCounter;
					info.packetCounters.clear();
				}
			}
			else if (Overflow.sub(lastBlockIdRead, blockId) > Constants.MaximumBlocksUntilResync) {
				// TODO(gpascualg): Flag resync
				return false;
			}

			if (info.packetCounters.contains(id)) {
				return false;
			}
			
			info.packetCounters.add(id);
		}
		else {
			ResolvedBlock info = new ResolvedBlock(loopCounter);
			info.packetCounters.add(id);
			alreadyResolved.put(blockId, info);
		}
		
		return true;
	}
}
