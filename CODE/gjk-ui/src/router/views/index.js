
import Layout from '@/page/index/'
import projectMain from '@/views/pro/project/manager'
import compMain from '@/views/comp/component/showComp'
let meta = { auth: true }
export default [{
    path: '/wel',
    component: Layout,
    redirect: '/wel/index',
    children: [{
        path: 'index',
        name: '首页',
        component: () =>
            import( /* webpackChunkName: "views" */ '@/page/wel')
    }]
}, {
    path: '/info',
    component: Layout,
    redirect: '/info/index',
    children: [{
        path: 'index',
        name: '个人信息',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/admin/user/info'),
    }]
}, {
    path: '/comp/showComp',
    component: Layout,
    redirect: '/comp/showComp/index',
    children: [{
        path: 'index',
        name: '构件信息',
        component: () => import('@/views/comp/component/index')
    },{
        path: 'commView',
        name: '查看构件',
        component: () => import('@/views/libs/commoncomponent/commView')
    },
    {
        path: '/comp/showComp',
        component: compMain,
        redirect: '/comp/showComp/addComp',
        children: [{
            path: 'addComp',
            name: '构件建模文件',
            component: () => import('@/views/comp/component/add'),
            meta: {
                keepAlive: true // 需要被缓存
            }
        }, {
            path: 'codeEditor',
            name: '编辑',
            component: () => import('@/views/comp/component/textEdit')
        }, {
            path: 'addAndEditComp',
            name: 'addAndEditComp',
            component: () => import('@/views/comp/code-editor/comp-params'),
            meta: {
                keepAlive: true,// 需要被缓存src\views\comp\code-editor\comp-params
            }
        }]
    },
    {
        path: '/comp/manager',
        component: projectMain,
        redirect: '/comp/manager',
        meta,
        children: (pro => [{
            path: 'showproject',
            name: '项目信息',
            component: () => import('@/views/pro/project/showproject')
        }, {
            path: 'process',
            name: 'process',
            component: () => import('@/views/pro/jsplumb/index'),
            meta: {
                // title1: `${pro}`,
                title: '流程模型'
            }
        }, {
            path: 'simulator',
            name: '仿真',
            component: () => import('@/views/pro/project/Simulation/simulation'),
        },{
            path: 'codeEditor',
            name: '编辑',
            component: () => import('@/page/common/codeEditor')
        }, {
            path: 'textEditors',
            name: '文本编辑',
            component: () => import('@/views/pro/project/textEdits')
        }, {
            path: 'fileProview',
            name: '文件预览',
            component: () => import('@/views/pro/project/fileProview')
        }, {
            path: 'showindex',
            name: '项目信息',
            component: () =>
                import('@/views/pro/project/showIndex')
        }, {
            path: 'dispose',
            name: '软硬件映射配置',
            component: () =>
                import('@/views/admin/dispose/index')
        }, {
            path: 'showPlan',
            name: '方案展示',
            component: () =>
                import('@/views/pro/project/showPlan')
        }, {
            path: 'sysConfiguration',
            name: '系统配置',
            component: () =>
                import('@/views/pro/project/sysConfiguration')
        }, {
            path: 'hardware',
            name: 'hardware',
            component: () =>
                import('@/views/pro/hardware/index'),
            meta: {
                // $keepAlive: true,
                title: '硬件建模'
            }
        }, {
            path: 'hardwareUpdate',
            name: 'hardwareUpdate',
            component: () =>
                import('@/views/pro/hardware/update'),
            meta: {
                // $keepAlive: true,
                title: '硬件模型编辑'
            }
        }, {
            path: 'hardwareAdd',
            name: 'hardwareAdd',
            component: () =>
                import('@/views/pro/hardware/add'),
            meta: {
                // $keepAlive: true,
                title: '硬件模型新增'
            }
        }, {
            path: 'customConfiguration',
            name: '自定义配置',
            component: () =>
                import('@/views/pro/project/CustomConfiguration')
        }, {
            path: 'deployment',
            name: 'deployment',
            component: () =>
                import('@/views/pro/deployment/index'),
            meta: {
                // $keepAlive: true,
                title: '部署图'
            }
        },
        ])('00000')
    }]

}, {
    path: '/libs/hardwarelibinf',
    component: Layout,
    redirect: '/libs/addinf',
    children: [{
        path: 'addinf',
        name: '接口新增',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibinf/addInf'),
    }]
}, {
    path: '/libs/hardwarelibchip',
    component: Layout,
    redirect: '/libs/addchip',
    children: [{
        path: 'chipdesign',
        name: 'chipdesign',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibchip/chipdesign'),
        meta: {
            // $keepAlive: true,
            title: '芯片新增'
        }
    }, {
        path: 'chipupdate',
        name: 'chipupdate',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibchip/chipupdate'),
        meta: {
            // $keepAlive: true,
            title: '芯片编辑',
            chipDataTemp: ''
        },
    }, {
        path: 'chipList',
        name: '芯片列表',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibchip/index'),
    }]
}, {
    path: '/libs/hardwarelibboard',
    component: Layout,
    redirect: '/libs/addboard',
    children: [{
        path: 'boarddesign',
        name: 'boarddesign',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibboard/boarddesign'),
        meta: {
            // $keepAlive: true,
            title: '板卡新增'
        }
    }, {
        path: 'boardupdate',
        name: 'boardupdate',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibboard/boardupdate'),
        meta: {
            // $keepAlive: true,
            title: '板卡编辑'
        }
    }]
}, {
    path: '/libs/hardwarelibcase',
    component: Layout,
    redirect: '/libs/addcase',
    children: [{
        path: 'casedesign',
        name: 'casedesign',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibcase/casedesign'),
        meta: {
            // $keepAlive: true,
            title: '机箱新增'
        }
    }, {
        path: 'caseupdate',
        name: 'caseupdate',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/hardwarelibcase/caseupdate'),
        meta: {
            // $keepAlive: true,
            title: '机箱编辑'
        }
    }]
},{
    path: '/libs/commoncomponent',
    component: Layout,
    redirect: '/libs/commoncomponent/index',
    children: [{
        path: 'batchExportList',
        name: 'batchExportList',
        component: () =>
            import( /* webpackChunkName: "page" */ '@/views/libs/commoncomponent/batchExportList'),
        meta: {
            // $keepAlive: true,
            title: '申请列表'
        }
    }]
},{
    path: '/admin/basetemplate12',
    name: "基础模板管理",
    component: Layout,
    //component: basetemplate,
    //redirect: '/wel/index',
    children: [
        {
            path: '/basetemplate/addTemplate',
            name: '基础模板新增功能',
            component: () =>
                import('@/views/admin/basetemplate/addTemplate'),

        },
        {
            path: '/basetemplate/editTemplate',
            name: '基础模板编辑模板',
            component: () =>
                import('@/views/admin/basetemplate/editTemplate'),

        },
    ]

},{
    path: '/libs/approval',
    name: "审批管理",
    component: Layout,
    //component: basetemplate,
    //redirect: '/wel/index',
    children: [
        {
            path: 'applyDetail',
            name: 'applyDetail',
            component: () =>
                import( /* webpackChunkName: "page" */ '@/views/libs/approval/applyDetail'),
            meta: {
                // $keepAlive: true,
                title: '审批详情'
            }
        },
    ]

}
// ,{
//     path: 'applyDetail',
//     name: 'applyDetail',
//     component: () =>
//         import( /* webpackChunkName: "page" */ '@/views/libs/commoncomponent/applyDetail'),
//     meta: {
//         // $keepAlive: true,
//         title: '审批详情'
//     }
// },

]
