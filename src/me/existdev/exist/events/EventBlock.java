package me.existdev.exist.events;

import java.util.ArrayList;
import java.util.List;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EventBlock extends EventCancellable implements Event {
	
    private BlockPos pos;
    private Block block;
    private AxisAlignedBB aabb;
    private List<AxisAlignedBB> axisAlignedBBList = new ArrayList<>();

    public EventBlock(BlockPos pos, Block block, AxisAlignedBB aabb) {
        this.pos = pos;
        this.block = block;
        this.aabb = aabb;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public AxisAlignedBB getAabb() {
        return aabb;
    }

    public void setAabb(AxisAlignedBB aabb) {
        this.aabb = aabb;
    }

    public List<AxisAlignedBB> getAxisAlignedBBList() {
        return axisAlignedBBList;
    }


}
