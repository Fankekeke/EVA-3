package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Room;
import cc.mrbird.febs.cos.service.IRoomService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/room")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomController {

    private final IRoomService roomService;

    @Log("查询房间信息")
    @GetMapping
    public R page(Page page, Room room) {
        return R.ok(roomService.getRoomPage(page, room));
    }

    @Log("添加房间")
    @PostMapping
    public R save(@RequestBody Room room) {
        return R.ok(roomService.save(room));
    }

    @Log("修改房间信息")
    @PutMapping
    public R update(@RequestBody Room room) {
        return R.ok(roomService.updateById(room));
    }

    @Log("房间上下架")
    @GetMapping("/onPut")
    public R onPut(Integer roomId, Integer flag) {
        return R.ok(roomService.update(Wrappers.<Room>lambdaUpdate().set(Room::getOnPut,flag).eq(Room::getId, roomId)));
    }

    @Log("删除房间信息")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(roomService.removeByIds(ids));
    }

}
