package com.chuqiyun.proxmoxveams.controller.admin;

import com.chuqiyun.proxmoxveams.annotation.AdminApiCheck;
import com.chuqiyun.proxmoxveams.common.ResponseResult;
import com.chuqiyun.proxmoxveams.common.exception.UnauthorizedException;
import com.chuqiyun.proxmoxveams.service.VmInfoService;
import com.chuqiyun.proxmoxveams.service.VmhostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author mryunqi
 * @date 2023/8/28
 */
@RestController
@RequestMapping("/{adminPath}")
public class SysVmHostInfoController {
    @Resource
    private VmInfoService vmInfoService;
    @Resource
    private VmhostService vmhostService;

    /**
    * @Author: mryunqi
    * @Description: 获取虚拟机主机信息
    * @DateTime: 2023/8/28 17:17
    */
    @AdminApiCheck
    @GetMapping(value = "/getVmHostInfo")
    public Object getVmHostInfo(@RequestParam(name = "hostId",defaultValue="0") Integer hostId) throws UnauthorizedException {
        // 判断参数是否为0
        if (hostId == 0) {
            return ResponseResult.fail("参数不能为空");
        }
        return ResponseResult.ok(vmInfoService.getVmHostById(hostId));
    }

    /**
    * @Author: mryunqi
    * @Description: 获取虚拟机历史负载
    * @DateTime: 2023/8/28 20:08
    */
    @AdminApiCheck
    @GetMapping(value = "/getVmHostRrdData")
    public Object getVmHostRrdData(@RequestParam(name = "hostId") Integer hostId,
                                   @RequestParam(name = "timeframe",defaultValue = "hour") String timeframe,
                                   @RequestParam(name = "cf",defaultValue = "AVERAGE") String cf) throws UnauthorizedException {
        return ResponseResult.ok(vmInfoService.getVmInfoRrdData(hostId,timeframe, cf).getJSONArray("data"));
    }

    /**
    * @Author: mryunqi
    * @Description: 获取虚拟机总数
    * @DateTime: 2023/11/26 20:20
    */
    @AdminApiCheck
    @GetMapping(value = "/getVmCount")
    public Object getVmCount() throws UnauthorizedException {
        return ResponseResult.ok(vmInfoService.getVmCount());
    }

    /**
    * @Author: mryunqi
    * @Description: 分页获取指定状态的虚拟机列表
    * @DateTime: 2023/11/26 22:02
    */
    @AdminApiCheck
    @GetMapping(value = "/getVmByStatus")
    public Object getVmByStatus(@RequestParam(name = "status") Integer status,
                                @RequestParam(name = "page",defaultValue = "1") Long page,
                                @RequestParam(name = "size",defaultValue = "20") Long size) throws UnauthorizedException {
        return ResponseResult.ok(vmhostService.getVmhostByStatus(page,size,status));
    }

    /**
    * @Author: mryunqi
    * @Description: 获取指定状态的虚拟机总数
    * @DateTime: 2023/11/26 22:11
    */
    @AdminApiCheck
    @GetMapping(value = "/getVmCountByStatus")
    public Object getVmCountByStatus(@RequestParam(name = "status") Integer status) throws UnauthorizedException {
        return ResponseResult.ok(vmhostService.getVmhostCountByStatus(status));
    }
}
