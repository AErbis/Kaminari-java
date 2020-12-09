package net.kaminari;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SuperPacket<PQ extends IProtocolQueues> {
	
	public Short id;
	private ArrayList<Short> pendingAcks;
	private Buffer buffer;
	private PQ queues;
	
	public Buffer getBuffer() {
		return buffer;
	}
	
	public SuperPacket(PQ queues) {
		id = 0;
		pendingAcks = new ArrayList<Short>();
		buffer = new Buffer();
		
		this.queues = queues;
		queues.reset();
	}
	
	public PQ getQueues() {
		return queues;
	}
	
	public void scheduleAck(Short blockId) {
		pendingAcks.add(blockId);
	}
	
	public boolean finish() {
		buffer.reset();
		
		//  First two bytes are size, next to id
		buffer.write((short)0);
		buffer.write(id);
		
		buffer.write((byte)pendingAcks.size());	
		boolean hasAcks = pendingAcks.size() > 0;
		for(Short ack : pendingAcks) {
			buffer.write(ack);
		}
		
        // Clear acks
        pendingAcks.clear();

        //  -1 is to account for the number of blocks
        Ref<Short> remaining = new Ref<Short>();
        remaining.value = (short)(500 - buffer.getPosition() - 1);
        
        
        // Organize packets that must be resent until ack'ed
        TreeMap<Integer, ArrayList<Packet>> by_block = new TreeMap<Integer, ArrayList<Packet>>();
        
        queues.process(id, remaining, by_block);

        // Write number of blocks
        buffer.write((byte)by_block.size());

        // Has there been any overflow? That can be detected by, for example
        //  checking max-min>thr
        boolean hasData = by_block.size() != 0;
        if (hasData){
            Integer max = by_block.lastKey();
            Integer threshold = (int) Short.MAX_VALUE; // sizeof(uint16_t) / 2 - 1, close enough

            for (Integer key: by_block.keySet()){
                if (max - key < threshold) {
                    break;
                }

                // Overflows are masked higher, so that they get pushed to the end
                //  of the map
                Integer masked = (1 << 16) | key;
                by_block.put(masked, by_block.get(key));
                by_block.remove(key);
            }

            byte counter = 0;
        
            // Write in packets
            for (Map.Entry<Integer, ArrayList<Packet>> entry : by_block.entrySet()){
                buffer.write((short)(entry.getKey() & 0xffff));
                buffer.write((byte)entry.getValue().size());

                for (Packet packet: entry.getValue()){
                    if (entry.getKey() == (int)id){
                        packet.finish(counter++);
                    }

                    buffer.write(packet);
                }
            }
        }
        
        buffer.write(0, (byte)buffer.getPosition());	
        ++id;
        return hasAcks || hasData;
	}
}
