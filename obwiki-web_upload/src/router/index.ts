import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import store from "@/store";
import {Tool} from "@/utils/tool";
import {message} from "ant-design-vue";

const routes: Array<any> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: () => import('../views/admin/admin-ebook.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: () => import('../views/admin/admin-user.vue')
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: () => import('../views/admin/admin-doc.vue')
  },
  {
    path: '/doc',
    name: 'Doc',
    component: () => import('../views/DocView.vue')
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: () => import('../views/admin/admin-category.vue')
  }
] 

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})



export default router
