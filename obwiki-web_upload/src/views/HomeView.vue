<template>
  <div>
    <a-layout style="padding: 24px 0; background: #fff">
      <a-layout-sider width="200" style="background: #fff">
        <a-menu mode="inline" style="height: 100%" @click="handleClick">
          <a-menu-item key="welcome">
            <MailOutlined />
            <span>欢迎</span>
          </a-menu-item>
          <a-sub-menu v-for="item in level1" :key="item.id">
            <template v-slot:title>
              <span><user-outlined />{{ item.name }}</span>
            </template>
            <a-menu-item v-for="child in item.children" :key="child.id">
              <MailOutlined /><span>{{ child.name }}</span>
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </a-layout-sider>
      <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">
        <!-- 欢迎页面 -->
        <div class="welcome" v-show="isShowWelcome">
          <h1>欢迎</h1>
        </div>

        <!-- 电子书列表 -->
        <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :grid="{
          gutter: 20, column: 3
        }" :data-source="ebooks">
          <template #renderItem="{ item }">
            <a-list-item key="item.name">
              <template #actions>
                <span>
                  <component :is="'FileOutlined'" style="margin-right: 8px" />
                  {{ item.docCount }}
                </span>
                <span>
                  <component :is="'UserOutlined'" style="margin-right: 8px" />
                  {{ item.viewCount }}
                </span>
                <span>
                  <component :is="'LikeOutlined'" style="margin-right: 8px" />
                  {{ item.voteCount }}
                </span>
              </template>
              <a-list-item-meta :description="item.description">
                <template #title>
                  <router-link :to="'/doc?ebookId=' + item.id">
                    {{ item.name }}
                  </router-link>
                </template>
                <template #avatar><a-avatar :src="item.cover" /></template>
              </a-list-item-meta>
            </a-list-item>
          </template>
        </a-list>
      </a-layout-content>
    </a-layout>
  </div>
</template>
<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from "axios";
import { Tool } from '@/utils/tool';
import { message } from 'ant-design-vue';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons-vue';
//定义响应式数据
const ebooks = ref();
// //完成渲染后执行
// onMounted(() => {
//   handleQueryEbook();
// })
// //修改查询方法 携带分类id参数
// const handleQueryEbook = () => {
//   axios.get("/ebook/list", {
//     params: {
//       page: 1,
//       size: 1000,
//       categoryId2: category2Id
//     }
//   }).then((resp) => {
//     ebooks.value = resp.data.content.list;
//   })
// }
//完成渲染后执行
// onMounted(() => {
//   handleQueryEbook();
// })
//修改查询方法 携带分类id参数
const handleQueryEbook = () => {
  console.log("查询分类 ID:", category2Id);  // 调试日志
  axios.get("/ebook/list", {
    params: {
      page: 1,
      size: 1000,
      categoryId2: category2Id
    }
  }).then((resp) => {
    console.log("后端返回:", resp.data);  // 调试日志看实际返回结构
    if (resp.data.success) {
      ebooks.value = resp.data.content.list || resp.data.content;  // 兼容两种结构
      console.log("赋值后的 ebooks:", ebooks.value);
    }
  }).catch((err) => {
    console.error("查询失败:", err);
  })
}
const isShowWelcome = ref(true);
let category2Id = 0;
//点击分类导航栏 获取选中id,发送查询请求
const handleClick = (value: any) => {
  if (value.key === 'welcome') {
    isShowWelcome.value = true;
  } else {
    category2Id = value.key;
    isShowWelcome.value = false;
    handleQueryEbook();
  }
}
const actions: Record<string, any>[] = [
  { icon: StarOutlined, text: '156' },
  { icon: LikeOutlined, text: '156' },
  { icon: MessageOutlined, text: '2' },
];
onMounted(() => {
  handleQueryCategory();
})
/*
* 分类相关
* */
const level1 = ref();
const handleQueryCategory = () => {
  axios.get("/category/all").then((resp) => {
    const data = resp.data;
    if (data.success) {
      console.log("原始数组", data.content);
      level1.value = [];
      level1.value = Tool.array2Tree(data.content, 0);
      console.log("树形结构：", level1);
    } else {
      message.error(data.message);
    }
  });
};

</script>