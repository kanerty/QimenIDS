package com.chuqiyun.proxmoxveams.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chuqiyun.proxmoxveams.dto.VmHostDto;
import com.chuqiyun.proxmoxveams.entity.Master;
import com.chuqiyun.proxmoxveams.entity.Vmhost;
import com.chuqiyun.proxmoxveams.service.MasterService;
import com.chuqiyun.proxmoxveams.service.VmInfoService;
import com.chuqiyun.proxmoxveams.service.VmhostService;
import com.chuqiyun.proxmoxveams.utils.ProxmoxApiUtil;
import com.chuqiyun.proxmoxveams.utils.VmUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author mryunqi
 * @date 2023/8/23
 */
@Service("vmInfoService")
public class VmInfoServiceImpl implements VmInfoService {
    @Resource
    private MasterService masterService;
    @Resource
    private VmhostService vmhostService;

    /**
    * @Author: mryunqi
    * @Description: 获取虚拟机分页列表
    * @DateTime: 2023/8/23 21:50
    * @Params: Integer page 页码，Integer size 每页数量
    * @Return HashMap<String, Object> 分页列表
    */
    @Override
    public HashMap<String, Object> getVmByPage(Integer page, Integer size) {
        Page<Vmhost> vmhostPage = vmhostService.selectPage(page, size);
        HashMap<String, Object> pageMap = new HashMap<>();
        buildVmHostDto(vmhostPage,pageMap);
        return pageMap;
    }

    /**
    * @Author: mryunqi
    * @Description: 分页查询指定参数的虚拟机
    * @DateTime: 2023/8/24 16:07
    * @Params: Integer page 页码，Integer size 每页数量，String param 查询参数，String value 查询值
    * @Return HashMap<String,Object> 分页列表
    */
    @Override
    public Object getVmHostPageByParam(Integer page, Integer size, String param, String value) {
        // 如果参数为空，则查询全部
        if (param == null || value == null){
            return getVmByPage(page, size);
        }
        // 如果参数为IP
        if (param.equals("ip")){
            Page<Vmhost> vmhostPage = vmhostService.selectPageByIp(page, size, value);
            HashMap<String, Object> pageMap = new HashMap<>();
            buildVmHostDto(vmhostPage,pageMap);
            return pageMap;
        }
        // 如果参数为主机名
        if (param.equals("name")){
            return vmhostService.getVmhostByName(value);
        }
        // 如果参数为虚拟机ID
        if (param.equals("vmid")){
            return vmhostService.getVmhostByVmId(Integer.parseInt(value));
        }
        // 如果参数为节点ID
        if (param.equals("nodeid")){
            Page<Vmhost> vmhostPage = vmhostService.selectPageByNodeId(page, size, value);
            HashMap<String, Object> pageMap = new HashMap<>();
            buildVmHostDto(vmhostPage,pageMap);
            return pageMap;
        }
        // 如果参数为状态
        if (param.equals("status")){
            Integer status = VmUtil.statusStrToInt(value);
            Page<Vmhost> vmhostPage = vmhostService.selectPageByStatus(page, size, status);
            HashMap<String, Object> pageMap = new HashMap<>();
            buildVmHostDto(vmhostPage,pageMap);
            return pageMap;
        }
        return getVmByPage(page, size);
    }

    private void buildVmHostDto(Page<Vmhost> vmhostPage,HashMap<String, Object> pageMap){
        List<Vmhost> vmhostList = vmhostPage.getRecords();
        List<VmHostDto> vmHostDtoList = new ArrayList<>();
        // 遍历虚拟机列表
        for (Vmhost vmhost : vmhostList){
            int nodeId = vmhost.getNodeid();
            int vmId = vmhost.getVmid();
            VmHostDto vmHostDto = new VmHostDto();
            JSONObject vmInfo;
            Master node = masterService.getById(nodeId);
            // 判断node是否存在，或者是否可用
            if (node == null || node.getStatus() != 0){
                vmHostDto.setVmhost(vmhost);
                vmHostDto.setCurrent(null);
                vmHostDtoList.add(vmHostDto);
                continue;
            }
            // 获取cookie
            HashMap<String, String> cookieMap = masterService.getMasterCookieMap(nodeId);
            ProxmoxApiUtil proxmoxApiUtil = new ProxmoxApiUtil();
            // 获取虚拟机实时信息
            try {
                vmInfo = proxmoxApiUtil.getVmStatus(node, cookieMap, vmId);
            } catch (Exception e) {
                vmInfo = null;
            }
            vmHostDto.setVmhost(vmhost);
            vmHostDto.setCurrent(vmInfo);
            vmHostDtoList.add(vmHostDto);
        }
        // 将Page对象转换为Map
        pageMap.put("total", vmhostPage.getTotal());
        pageMap.put("size", vmhostPage.getSize());
        pageMap.put("current", vmhostPage.getCurrent());
        pageMap.put("pages", vmhostPage.getPages());
        pageMap.put("records", vmHostDtoList);
    }
}
