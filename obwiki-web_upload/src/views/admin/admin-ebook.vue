<template>
    <div>
        <a-layout>
            <a-layout-content :style="{
                background: '#fff', padding: '24px', margin: 0, minHeight:
                    '280px'
            }">
                <p>
                    <a-form layout="inline" :model="param">
                        <a-form-item>
                            <a-input v-model:value="param.name" placeholder="名称">
                            </a-input>
                        </a-form-item>
                        <a-form-item>
                            <a-button type="primary" @click="handleQuery({
                                page: 1, size:
                                    pagination.pageSize
                            })">
                                查询
                            </a-button>
                        </a-form-item>
                        <a-form-item>
                            <a-button type="primary" @click="add()">
                                新增
                            </a-button>
                        </a-form-item>
                    </a-form>
                </p>
                <a-table :columns="columns" :row-key="record => record.id" :data-source="ebooks"
                    :pagination="pagination" :loading="loading" @change="handleTableChange">
                    <template #bodyCell="{ column, record }">
                        <template v-if="column.key === 'cover'">
                            <a-image :src="record.cover" alt="图片加载失败" style="width:80px;height:80px" />
                        </template>
                        <template v-if="column.key === 'category'">
                            <span>
                                {{ getCategoryName(record.category1Id) }}/{{ getCategoryName(record.category2Id) }}
                            </span>
                        </template>
                        <template v-if="column.dataIndex === 'action'">
                            <a-space size="small">
                               <a-button type="primary">
                                    <router-link :to="'/admin/doc?ebookId=' + record.id"> 文档管理
                                    </router-link>
                                </a-button>
                                <a-button type="primary" @click="edit(record)">
                                    编辑
                                </a-button>
                                <a-popconfirm title="删除后不可以恢复，确认删除?" ok-text="是" cancel-text="否"
                                    @confirm="handleDelete(record.id)">
                                    <a-button type="danger">
                                        删除
                                    </a-button>
                                </a-popconfirm>
                            </a-space>
                        </template>
                    </template>
                </a-table>
            </a-layout-content>
        </a-layout>
        <a-modal title="电子书表单" v-model:visible="modalVisible" :confirm-loading="modalLoading" @ok="handleModalOk">
            <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                <a-form-item label="封面">
                    <a-input v-model:value="ebook.cover" />
                </a-form-item>
                <a-form-item label="名称">
                    <a-input v-model:value="ebook.name" />
                </a-form-item>
                <a-form-item label="分类">
                    <a-cascader v-model:value="categoryIds"
                        :field-names="{ label: 'name', value: 'id', children: 'children' }" :options="level1" />
                </a-form-item>
                <a-form-item label="描述">
                    <a-textarea v-model:value="ebook.description" type="textarea" />
                </a-form-item>
            </a-form>
        </a-modal>
    </div>

</template>

<script lang="ts" setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { Tool } from "@/utils/tool";
import {message} from "ant-design-vue";


const ebooks = ref();//定义查询电子书返回集合
//定义分页参数
const pagination = ref({
    current: 1,
    pageSize: 5,
    total: 0
});
const loading = ref(false);
const columns = [
    {
        title: '封面',
        key: 'cover',
        dataIndex: 'cover',
    },
    {
        title: '名称',
        dataIndex: 'name'
    },
    {
        title: '分类一',
        key: 'category',
        dataIndex: 'category1Id'
    },
    {
        title: '分类二',
        dataIndex: 'category2Id'
    },
    {
        title: '文档数',
        dataIndex: 'docCount'
    },
    {
        title: '阅读数',
        dataIndex: 'viewCount'
    },
    {
        title: '点赞数',
        dataIndex: 'voteCount'
    },
    {
        title: 'Action',
        key: 'action',
        dataIndex: 'action'
    }
];
onMounted(() => {
    //默认查询页码为1
    handleQuery({
        page: 1,
        size: pagination.value.pageSize
    });
})
/*
* 数据查询
* */
const param = ref();
param.value = {};
const handleQuery = (params: any) => {
    loading.value = true;
    axios.get("/ebook/listByPage", {
        params: {
            page: params.page,
            size: params.size,
            name: param.value.name
        }
    }).then((resp) => {
        loading.value = false;
        const data = resp.data;
        if (data.success) {
            //获取查询数据
            ebooks.value = data.content.list;
            //重置分页按钮
            pagination.value.current = params.page;
            //设置总条数
            pagination.value.total = data.content.total;
        }
    });
};

/*
* 表格点击页码时触发
* */
const handleTableChange = (pagination: any) => {
    console.log("看看自带分页的参数都有些啥：" + pagination);
    handleQuery({
        page: pagination.current,
        size: pagination.pageSize
    });
};

// 编辑相关功能
const ebook = ref();
const modalVisible = ref(false);
const modalLoading = ref(false);
//编辑
//编辑
const edit = (record: any) => {
    modalVisible.value = true;
    ebook.value = Tool.copy(record);
    categoryIds.value = categoryIds.value =
        [ebook.value.category1Id, ebook.value.category2Id]
}
// const getCategoryName = (cid: string) => {
//     //传递的cid为number类型 与categorys中的id为string类型 需要修改
//     let cidstr: string = cid.toString();
//     let result = "";
//     categorys.forEach((item: string) => {
//         if (item.id === cidstr) {
//             result = item.name;
//         }
//     });
//     return result;
// };
const getCategoryName = (cid: any) => {
    if (!cid || !categorys || categorys.length === 0) {
        return "";
    }

    const cidstr = cid.toString();
    let result = "";

    categorys.forEach((item: any) => {
        if (item.id.toString() === cidstr) {
            result = item.name;
        }
    });

    return result;
};


//新增
const add = () => {
    modalVisible.value = true;
    //清空对话框数据
    ebook.value = {};
};

//删除
const handleDelete = (id: number) => {
    axios.delete('/ebook/remove/' + id).then((resp) => {
        if (resp.data.success) {
            //重新加载列表
            handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
            });
        }
    })
};

const handleModalOk = () => {
    modalLoading.value = true;
    //获取分类1 及分类2的值
    ebook.value.category1Id = categoryIds.value[0];
    ebook.value.category2Id = categoryIds.value[1];
    console.log(ebook.value);
    axios.post("/ebook/save", ebook.value).then((resp) => {
        const data = resp.data;
        if (data.success) {
            modalVisible.value = false;
            modalLoading.value = false;
            //重新加载列表
            handleQuery({
                page: pagination.value.current,
                size: pagination.value.pageSize
            })
        } else {
            message.error(data.message);
        }
    })
};



onMounted(() => {
    handleQueryCategory();
})
//查询分类数据级联展示
const categoryIds = ref(); //保存选中分类数组[100:101] 对应海洋植物:藻类植物
const level1 = ref();//保存分类数据
let categorys: any[] = [];
const handleQueryCategory = () => {
    loading.value = true;
    axios.get("/category/all").then((resp) => {
        loading.value = false;
        const data = resp.data;
        categorys = data.content;
        if (data.success) {
            console.log("原始数组", categorys);
            level1.value = [];
            level1.value = Tool.array2Tree(categorys, 0);
            console.log("树形结构：", level1);
            //加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书的渲染会报错
            handleQuery({
                page: 1,
                size: pagination.value.pageSize
            });
        } else {
            message.error(data.message);
        }
    });
};



</script>