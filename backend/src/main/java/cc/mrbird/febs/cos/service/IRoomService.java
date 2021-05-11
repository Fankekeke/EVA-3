package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IRoomService extends IService<Room> {

    // 分页查询房间信息
    IPage<LinkedHashMap<String, Object>> getRoomPage(Page page, Room room);

    // 查询所有房间类别
    List<LinkedHashMap<String, Object>> roomList();
}
