package com.ghovos.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ghovos.yygh.common.exception.YyghException;
import com.ghovos.yygh.common.result.Result;
import com.ghovos.yygh.common.utils.MD5;
import com.ghovos.yygh.hosp.service.HospitalSetService;
import com.ghovos.yygh.model.hosp.HospitalSet;
import com.ghovos.yygh.vo.hosp.HospitalQueryVo;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    /**
     * 注入
     */
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 1. 查询医院设置表中所有信息
     *
     * @return 所有医院
     */
    @ApiOperation(value = "获取所有的医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 2. 根据id删除医院(逻辑删除)
     *
     * @param id 要删除的id
     * @return 是否删除成功
     */
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 3. 条件查询带分页
     *
     * @param current         当前页
     * @param limit           limit
     * @param hospitalQueryVo 查询条件
     * @return 分页结果
     */
    @PostMapping("findPage/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,       //当前页
                                  @PathVariable long limit,         // limit
                                  @RequestBody(required = false) HospitalQueryVo hospitalQueryVo) {
        //创建page对象，传递当前页
        Page<HospitalSet> page = new Page<>(current, limit);

        //构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();

        String hosname = hospitalQueryVo.getHosname();
        String hoscode = hospitalQueryVo.getHoscode();


        // 判断是否为空
        if (!StringUtils.isNullOrEmpty(hosname)) {
            wrapper.like("hosname", hospitalQueryVo.getHosname());
        }
        if (!StringUtils.isNullOrEmpty(hoscode)) {
            wrapper.eq("hoscode", hospitalQueryVo.getHoscode());

        }

        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);

        //返回结果
        return Result.ok(pageHospitalSet);
    }


    /**
     * 3. 添加医院设置
     *
     * @param hospitalSet 医院的json数据
     * @return 成功与否
     */
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态1 使用，  0 不使用
        hospitalSet.setStatus(1);

        // 设置签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));

        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }


    /**
     * 5. 根据id获取医院设置
     *
     * @param id 查询的id
     */
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {

        try {
            int a = 1 / 0;
        }catch (Exception e){
            throw new YyghException("除数为0",201);
        }


        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }


    /**
     * 6. 修改医院设置
     *
     * @param hospitalSet 医院json实体
     */
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {


        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }


    /**
     * 7. 批量删除医院设置
     *
     * @param idList id列表
     */
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        boolean flag = hospitalSetService.removeByIds(idList);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    /**
     * 8. 医院设置锁定和解锁
     */
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result locakHospitalSet(@PathVariable Long id,
                                   @PathVariable Integer status) {


        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);

        //更新信息
        hospitalSetService.updateById(hospitalSet);

        return Result.ok();

    }


    /**
     * 9. 发送签名密钥
     */
    @PutMapping("sendKey/{id}")
    public Result sendHospitalSetKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }


}
