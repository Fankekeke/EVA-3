package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Hotel;
import cc.mrbird.febs.cos.service.IHotelService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/hotel")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HotelController {

    private final IHotelService hotelService;

    @Log("获取酒店信息")
    @GetMapping("/info")
    public R getInfo() {
        return R.ok(hotelService.getById(1));
    }

    @Log("保存酒店信息")
    @PostMapping
    public R save(@RequestBody Hotel hotel) {
        return R.ok(hotelService.save(hotel));
    }

    @Log("修改酒店信息")
    @PutMapping
    public R update(@RequestBody Hotel hotel) {
        return R.ok(hotelService.updateById(hotel));
    }

}
