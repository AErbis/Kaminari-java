package net.kaminari;

import java.util.ArrayList;
import java.util.TreeMap;

public interface IProtocolQueues {
    void reset();
    void ack(Short blockId);
    void process(Short blockId, Ref<Short> remaining, TreeMap<Integer, ArrayList<Packet>> byBlock);
    
}
