from fastapi import APIRouter

from common.CodeEnum import CodeEnum
from common.ResponseResult import common_response

import service.Nat as Nat
from entity.NatForward import ForwardRule

nat_router = APIRouter()

'''
添加端口转发规则
Add port forwarding rules
'''
@nat_router.post('/nat/add')
async def nat_add(item: ForwardRule):
    nat = Nat.NatManager(item.source_port, item.destination_ip, item.destination_port, item.protocol, item.vm)
    result = nat.add_forward_rule()
    code = result['code']
    message = result['message']
    if (code == 0):
        return common_response(CodeEnum.SUCCESS, message, {})
    else:
        return common_response(CodeEnum.FAIL, message, {})

'''
删除端口转发规则
Delete port forwarding rules
'''
@nat_router.post('/nat/delete')
async def nat_delete(item: ForwardRule):
    nat = Nat.NatManager(item.source_port, item.destination_ip, item.destination_port, item.protocol, item.vm)
    result = nat.delete_forward_rule()
    code = result['code']
    message = result['message']
    if (code == 0):
        return common_response(CodeEnum.SUCCESS, message, {})
    else:
        return common_response(CodeEnum.FAIL, message, {})
    
'''
获取端口转发规则
Get port forwarding rules
'''
@nat_router.post('/nat/getVm/{page}/{size}')
async def nat_get(page: int, size: int, item: ForwardRule):
    nat = Nat.NatManager(item.source_port, item.destination_ip, item.destination_port, item.protocol, item.vm)
    result = nat.get_forward_rules_by_vm(page, size)
    code = result['code']
    message = result['message']
    if (code == 0):
        return common_response(CodeEnum.SUCCESS, message, result['data'])
    else:
        return common_response(CodeEnum.FAIL, message, {})