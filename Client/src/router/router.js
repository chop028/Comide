import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
    history: createWebHistory(""),
    scrollBehavior() {
        return {top:0}
    },
    routes: [
        {
            path: "/",
            name: "main",
            component: () => import("../views/Mainpage_T.vue"),
        },
        {
            path: "/board/:category",
            name: "board",
            component: () => import("../views/BoardList_T.vue"),
        },
        {
            path: "/post/write",
            name: "post",
            component: () => import("../views/PostPage_T.vue"),
        },
        {
            path: '/post/:id',
            name: 'PostDetail',
            component: () => import("../views/PostView_T.vue"),
        },
        {
            path: "/quiz/multi/:quizPostId",
            name: "quiz_multi",
            component: () => import("../views/Quiz_multi_T.vue"),
        },
        {
            path: "/quiz/short",
            name: "quiz_short",
            component: () => import("../views/Quiz_short_T.vue"),
        },
        {
            path: "/quiz/list/all",
            name: "quizlistall",
            component: () => import("../views/QuizList_all_T.vue"),
        },
        {
            path: "/quiz/list/info",
            name: "quizlistinfo",
            component: () => import("../views/QuizList_info_T.vue"),
        },
        {
            path: "/quiz/result/:quizResultId",
            name: "quizresult",
            component: () => import("../views/QuizResult_T.vue"),
        },
        {
            path: "/code_edit",
            name: "codeedit",
            component: () => import("../views/CodeEditer_T.vue"),
            props: route => ({ codeId: route.query.codeId })
        },
        {
            path: "/login",
            name: "login",
            component: () => import("../views/LoginPage.vue"),
        },
        {
            path: "/mypage/userinfo",
            name: "mypageuserinfo",
            component: () => import("../views/MypageUserinfo_T.vue"),
        },
        {
            path: "/mypage/activity",
            name: "mypageactivity",
            component: () => import("../views/MypageActivity_T.vue"),
        },
        {
            path: "/mypage/quiz",
            name: "mypagequiz",
            component: () => import("../views/MypageQuiz_T.vue"),
        },
        {
            path: "/mypage/option",
            name: "mypageoption",
            component: () => import("../views/MypageOption_T.vue"),
        },
        {
            path: "/mypage/alarm",
            name: "mypagealarm",
            component: () => import("../views/MypageAlarm_T.vue"),
        },
        {
            path: "/develop/knowlang/:category",
            name: "developint",
            component: () => import("../views/DevelopInt_T.vue"),
        },
        {
            path: "/develop/info",
            name: "developinfor",
            component: () => import("../views/DevelopInfor_T.vue"),
        },
        {
            path: "/develop/info/register",
            name: "developinforupload",
            component: () => import("../views/DevelopInforUpload_T.vue"),
        },
        {
            path: "/develop/info/write/user",
            name: "developinforuploadwrite",
            component: () => import("../views/DevelopInforUserWrite_T.vue"),
        },
        {
            path: '/admin/main',
            name: 'adminmain',
            component: () => import("../views/Admin_Mainpage_T.vue"),
        },
        {
            path: '/search',
            name: 'search',
            component: () => import("../views/Searchpage_T.vue"),
        },
        {
            path: '/makegroup',
            name: 'makegroup',
            component: () => import("../views/MakeGroup_Big_T.vue"),
        },
        {
            path: '/changegroup/option',
            name: 'changegroup_option',
            component: () => import("../views/ChangeGroupOption_Big.vue"),
            props: true,
        },
        {
            path: '/startquiz/:quizPostId',
            name: 'startquiz',
            component: () => import("../views/Quiz_Start_Page.vue"),
        },
        {
            path: '/rank',
            name: 'rank',
            component: () => import("../views/Rank_T.vue"),
        },
        {
            path: '/quiz/write/multi',
            name: 'quizwritemulti',
            component: () => import("../views/WriteQuiz_multi_T.vue"),
        },
        {
            path: '/quiz/write/short',
            name: 'quizwriteshort',
            component: () => import("../views/WriteQuiz_short_T.vue"),
        },
        {
            path: '/Admin/developinfor',
            name: 'developinforadmin',
            component: () => import("../views/DevelopInfor_Admin_T.vue"),
        },
        {
            path: '/Admin/LangKnwl/:category',
            name: 'langKnwl',
            component: () => import("../views/Admin_LangKnwl_T.vue"),
        },
        {
            path: '/Admin/LangKnwl/write',
            name: 'writeLangKnwl',
            component: () => import("../views/Admin_writeLangKnwl_T.vue"),
        },
        {
            path: '/LangKnwl/:id',
            name: 'LangKnwlDetail',
            component: () => import("../views/LangKnwlView.vue"),
        },
        {
            path: "/developinfor/write",
            name: "adminwritedevelopinfor",
            component: () => import("../views/Admin_DevelopInforWrite_T.vue"),
        },
    ],
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    const userInfo = JSON.parse(localStorage.getItem('userInfo'));
    const publicRoutes = ['main', 'login', 'redirect', 'developint', 'developinfor'];

    if (publicRoutes.includes(to.name)) {
        next();
    } else if (to.path.startsWith('/admin')) {
        if (token && userInfo && userInfo.role === 'ROLE_ADMIN') {
            next();
        } else {
            alert('권한이 없습니다');
            next({ path: '/' });
        }
    } else {
        if (token) {
            next();
        } else {
            alert('로그인이 필요합니다.');
            next({ path: '/login' });
        }
    }
});

export default router;