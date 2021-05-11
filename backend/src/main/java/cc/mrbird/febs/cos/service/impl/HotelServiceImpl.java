package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Hotel;
import cc.mrbird.febs.cos.dao.HotelMapper;
import cc.mrbird.febs.cos.service.IHotelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author FanK
 */
@Service
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

}
