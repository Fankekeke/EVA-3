package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RoomType;
import cc.mrbird.febs.cos.service.IRoomTypeService;
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
@RequestMapping("/cos/room-type")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomTypeController {

    private final IRoomTypeService roomTypeService;

    @Log("查询房间类型")
    @GetMapping
    public R page(Page page, RoomType roomType) {
        return R.ok(roomTypeService.page(page, Wrappers.<RoomType>lambdaQuery()
                .eq(RoomType::getDeleteFlag, 0)
                .like(roomType.getTypeName() != null && !("").equals(roomType.getTypeName()), RoomType::getTypeName, roomType.getTypeName())
                .like(roomType.getFacilities() != null && !("").equals(roomType.getFacilities()), RoomType::getFacilities, roomType.getFacilities())));
    }

    @Log("查询全部房间类型")
    @GetMapping("/list")
    public R list() {
        return R.ok(roomTypeService.list(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getDeleteFlag, 0)));
    }

    @Log("新增房间类型")
    @PostMapping
    public R save(@RequestBody RoomType roomType) {
        return R.ok(roomTypeService.save(roomType));
    }

    @Log("修改房间类型")
    @PutMapping
    public R update(@RequestBody RoomType roomType) {
        return R.ok(roomTypeService.updateById(roomType));
    }

    @Log("删除房间类型")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(roomTypeService.removeByIds(ids));
    }

}
