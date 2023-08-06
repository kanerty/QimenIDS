package com.chuqiyun.proxmoxveams.service;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chuqiyun.proxmoxveams.dto.VmParams;
import com.chuqiyun.proxmoxveams.entity.Vmhost;

import java.util.HashMap;

/**
 * (Vmhost)表服务接口
 *
 * @author mryunqi
 * @since 2023-06-21 15:11:18
 */
public interface VmhostService extends IService<Vmhost> {

    Vmhost getVmhostByVmId(int vmId);

    Page<Vmhost> selectPage(Integer page, Integer limit);

    Page<Vmhost> selectPage(Integer page, Integer limit, QueryWrapper<Vmhost> queryWrapper);

    Integer addVmhost(int vmId, VmParams vmParams);

    HashMap<String,Object> power(Integer hostId, String action);

    void syncVmStatus(JSONArray vmHosts, Integer nodeId);
}

