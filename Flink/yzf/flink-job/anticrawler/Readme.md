# 1、客户信息： ${request:contains('/portal/api/customerManager')}
查询会计准则本位币等信息 ${request:startsWith('GET /portal/api/customerManager/queryZtxxKjzdAndBwbAndKjhy')}
// 重点 翻页场景
查询企业列表(客户信息页面) ${request:startsWith('POST /portal/api/customerManager/queryCompanyList')}
// 第一次 进入 账套页面 高频 迭代
查询账套信息(客户信息页面) ${request:startsWith('GET /portal/api/customerManager/quertQyZtxx')}
# 2、凭证信息： ${request:contains('/pingzheng/api/')}
// 凭证
查询凭证列表数据 ${request:startsWith('GET /pingzheng/api/pz/listPz')}
// 重要
查询科目余额表1 ${request:startsWith('GET /pingzheng/api/zhangmu/listKemuYeM')}
// 重要
查询科目余额表2 ${request:startsWith('GET /pingzheng/api/zhangmu/listKemuYe')}
查询数量核算余额表 ${request:startsWith('GET /pingzheng/api/zhangmu/slhs/getKmyeSlhs')}
// 
# 3、主页面： ${request:contains('/portal/api/index/ajax')}
进入主页面 ${request:startsWith('POST /portal/api/index/ajax/mainOther')}
// 旧版本主页
企业列表旧接口 ${request:startsWith('POST /portal/api/index/ajax/homeQyListByCondition')}

企业列表新接口 ${request:startsWith('POST /portal/index/book/list')}
# 4、科目主页： ${request:contains('/system/api/system/ztkm')}
// 进入科目主页
查询凭证末级科目数据 ${request:startsWith('POST /system/api/system/ztkm/getAllPzMjkm')}
科目接口 ${request:startsWith('POST /system/api/system/ztkm/getAllZtkm')}