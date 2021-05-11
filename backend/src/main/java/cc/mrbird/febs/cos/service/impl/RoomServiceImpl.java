package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Room;
import cc.mrbird.febs.cos.dao.RoomMapper;
import cc.mrbird.febs.cos.service.IRoomService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getRoomPage(Page page, Room room) {
        return baseMapper.getRoomPage(page, room);
    }

    @Override
    public List<LinkedHashMap<String, Object>> roomList() {
        return baseMapper.roomList();
    }
}
