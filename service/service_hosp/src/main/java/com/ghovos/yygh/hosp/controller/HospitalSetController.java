package com.ghovos.yygh.hosp.controller;

import com.ghovos.yygh.hosp.service.HospitalSetService;
import com.ghovos.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return 所有医院
     */
    @GetMapping("findAll")
    public List<HospitalSet> findAllHospitalSet() {
        return hospitalSetService.list();
    }

    /**
     * 2. 根据id删除医院(逻辑删除)
     * @param id 要删除的id
     * @return 是否删除成功
     */
    @DeleteMapping("{id")
    public boolean removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        return flag;
    }




}
