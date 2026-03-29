<template>
    <div>
        <a-layout>
            <a-layout-content :style="{
                background: '#fff', padding: '24px', margin: 0, minHeight:
                    '280px'
            }">
                <a-row>
                    <a-col :span="8">
                        <p>
                            <a-form layout="inline" :model="param">
                                <a-form-item>
                                    <a-button type="primary" @click="add()">
                                        新增
                                    </a-button>
                                </a-form-item>
                            </a-form>
                        </p>
                        <a-table v-if="level1.length > 0" :columns="columns" :row-key="record => record.id"
                            :data-source="level1" :pagination="false" :loading="loading" :defaultExpandAllRows="true">
                            <template #bodyCell="{ column, record }">
                                <template v-if="column.dataIndex === 'action'">
                                    <a-space size="small">
                                        <a-button type="primary" @click="edit(record)">
                                            编辑
                                        </a-button>
                                        <a-popconfirm title="删除后不可以恢复，确认删除?" ok-text="是" cancel-text="否"
                                            @confirm="handleDelete(record.id)">
                                            <a-button danger>
                                                删除
                                            </a-button>
                                        </a-popconfirm>
                                    </a-space>
                                </template>
                            </template>
                        </a-table>
                    </a-col>
                    <a-col :span="16">
                        <p>
                            <a-form layout="inline">
                                <a-form-item>
                                    <a-button type="primary" @click="handleSave()">
                                        保存
                                    </a-button>
                                </a-form-item>
                            </a-form>
                        </p>
                        <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
                            <a-form-item>
                                <a-input v-model:value="doc.name" placeholder="名称" />
                            </a-form-item>
                            <a-form-item>
                                <a-tree-select v-model:value="doc.parent" show-search style="width: 100%"
                                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }" placeholder="请选择父文档"
                                    allow-clear tree-default-expand-all :tree-data="treeSelectData"
                                    tree-node-filter-prop="label" :replaceFields="{
                                        children: 'children', label: 'name', key: 'id', value: 'id'
                                    }">
                                </a-tree-select>
                            </a-form-item>
                            <a-form-item>
                                <a-select ref="select" v-model:value="doc.parent" style="width: 120px">
                                    <a-select-option value="0">无</a-select-option>
                                    <a-select-option v-for="c in level1" :key="c.id" :value="c.id"
                                        :disabled="doc.id === c.id">{{ c.name
                                        }}</a-select-option>
                                </a-select>
                            </a-form-item>
                            <a-form-item>
                                <a-input v-model:value="doc.sort" placeholder="顺序" />
                            </a-form-item>
                            <a-form-item>
                                <div style="border: 1px solid #ccc">
                                    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef"
                                        :defaultConfig="toolbarConfig" :mode="mode" />
                                    <Editor style="height: 500px; overflow-y: hidden;" v-model="doc.content"
                                        :defaultConfig="editorConfig" :mode="mode" @onCreated="handleCreated" />
                                </div>
                            </a-form-item>
                        </a-form>
                    </a-col>
                </a-row>
            </a-layout-content>
        </a-layout>
    </div>
</template>
<script lang="ts" setup>
import axios from 'axios';
import { ref, onMounted, shallowRef } from 'vue';
import { Tool } from "@/utils/tool.ts";
import { message } from 'ant-design-vue';
import { useRoute } from 'vue-router';
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
const route = useRoute();//声明路由
const docs = ref();//定义查询电子书返回集合
const param = ref();
param.value = {};
//定义分页参数
const pagination = ref({
    current: 1,
    pageSize: 2,
    total: 0
});
const loading = ref(false);
const columns = [
    {
        title: '名称',
        dataIndex: 'name'
    },
    {
        title: 'Action',
        key: 'action',
        dataIndex: 'action'
    },
    {
        title: '父分类',
        dataIndex: 'parent'
    },
    {
        title: '顺序',
        dataIndex: 'sort'
    }
];
onMounted(() => {
    handleQuery();
})
/*
* 数据查询
* */
const level1 = ref([]);
// 编辑相关功能
const doc = ref({
    id: 0,
    name: "",
    parent: 0,
    ebook_id: 0,
    sort: 0
});
const handleQuery = () => {
    loading.value = true;
    axios.get("/doc/all").then((resp) => {
        loading.value = false;
        const data = resp.data;
        if (data.success) {
            level1.value = [];//每次查询清空分类数组
            //通过工具类 递归父分类及子分类 参数1 分类所有数据 参数2 父分类id为0
            level1.value = Tool.array2Tree(data.content, 0);
            if (Tool.isNotEmpty(level1.value) && level1.value.length > 0) {
                doc.value = Tool.copy(level1.value[0]);
                // 将树数据用于选择器（若需要）
                treeSelectData.value = Tool.copy(level1.value);
                treeSelectData.value.unshift({ id: 0, name: '无' });
                // 加载第一个文档的内容到编辑器
                handleQueryContent();
            }
            console.log("树形结构：", level1);
        }
    });
};
// 编辑相关功能
const modalVisible = ref(false);
const modalLoading = ref(false);

const treeSelectData = ref();
treeSelectData.value = [];

//编辑
const edit = (record: any) => {
    doc.value = Tool.copy(record);
    modalVisible.value = true;
    handleQueryContent();//查询文档内容
    //不能选择当前节点及其子节点，作为父节点，会使得树断开
    treeSelectData.value = Tool.copy(level1.value);
    setDisable(treeSelectData.value, record.id);
    //为树选择添加一个“无”的选项
    treeSelectData.value.unshift({ id: 0, name: '无' });
}
//内容查询
const handleQueryContent = () => {
    axios.get("/content/findContent/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
            editorRef.value.setHtml(data.content)
        } else {
            message.error(data.message);
        }
    });
};

/**
* 将某节点及其子孙节点全部置为disabled
*/
const setDisable = (treeSelectData: any, id: any) => {
    // console.log(treeSelectData, id);
    // 遍历数组，即遍历某一层节点
    for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
            // 如果当前节点就是目标节点
            console.log("disabled", node);
            // 将目标节点设置为disabled
            node.disabled = true;
            // 遍历所有子节点，将所有子节点全部都加上disabled
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
                for (let j = 0; j < children.length; j++) {
                    setDisable(children, children[j].id)
                }
            }
        } else {
            // 如果当前节点不是目标节点，则到其子节点再找找看。
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
                setDisable(children, id);
            }
        }
    }
}
//新增
const add = () => {
    modalVisible.value = true;
    doc.value = {
        ebookId: route.query.ebookId//获取电子书id
    };
    //不能选择当前节点及其子节点，作为父节点，会使得树断开
    treeSelectData.value = Tool.copy(level1.value);
    //为树选择添加一个“无”的选项
    treeSelectData.value.unshift({ id: 0, name: '无' });
}
// 查找整根树枝要删除的 id 列表与名称（先声明并在每次删除前清空）
const ids: Array<string> = [];
const deleteNames: Array<string> = [];

/**
 * 递归收集要删除的节点及其子节点 id
 */
const getDeleteIds = (treeSelectData: any, id: any) => {
    for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
            console.log("delete", node);
            ids.push(id);
            deleteNames.push(node.name);
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
                for (let j = 0; j < children.length; j++) {
                    getDeleteIds(children, children[j].id);
                }
            }
        } else {
            const children = node.children;
            if (Tool.isNotEmpty(children)) {
                getDeleteIds(children, id);
            }
        }
    }
};
//富文本编辑器
const editorRef = shallowRef();
// 编辑器回调函数
const handleCreated = (editor) => {
    console.log('created', editor);
    editorRef.value = editor; // 记录 editor 实例，重要！
};
// 删除（替换旧的 handleDelete）
const handleDelete = (id: number) => {
    // 清空数组，否则多次删除时会累积
    ids.length = 0;
    deleteNames.length = 0;
    // 收集要删除的 id（包括子孙）
    getDeleteIds(level1.value, id);
    // 可选：如果想提示要删除的节点名称，可在此使用 deleteNames
    axios.get('/doc/remove?ids=' + ids.join(",")).then((response) => {
        const data = response.data;
        if (data.success) {
            handleQuery();
        } else {
            message.error(data.message);
        }
    }).catch(err => {
        console.error('删除异常', err);
        message.error('删除失败');
    });
};
const handleSave = () => {
    modalLoading.value = true;
    doc.value.content = editorRef.value.getHtml();
    axios.post("/doc/save", doc.value).then((resp) => {
        const data = resp.data;
        if (data.success) {
            modalLoading.value = false;
            message.success("保存成功！");
            //重新加载列表
            handleQuery();
        }
    })
};
const handleModalOk = () => {
    modalLoading.value = true;
    // 简单前端校验：名称必填
    if (Tool.isEmpty(doc.value) || Tool.isEmpty(doc.value.name)) {
        modalLoading.value = false;
        message.error('名称为必填项');
        return;
    }

    // 设置默认 parent 为 0（无父节点）以防后端接收不到该字段
    if (Tool.isEmpty(doc.value.parent)) {
        doc.value.parent = 0;
    }

    // 确保 ebookId 来自路由或保持已有值
    if (Tool.isEmpty(doc.value.ebookId) && !Tool.isEmpty(route.query.ebookId)) {
        doc.value.ebookId = route.query.ebookId;
    }
    // 规范化 id 字段：TreeSelect 或路由可能返回类似 "0-2" 的路径字符串，后端期待 Long
    const normalizeId = (val: any) => {
        if (Tool.isEmpty(val)) return 0;
        if (typeof val === 'number') return val;
        if (typeof val === 'string') {
            if (val.indexOf('-') >= 0) {
                const parts = val.split('-');
                return Number(parts[parts.length - 1]);
            }
            const n = Number(val);
            return isNaN(n) ? 0 : n;
        }
        return val;
    }

    doc.value.parent = normalizeId(doc.value.parent);
    if (Tool.isNotEmpty(doc.value.ebookId)) {
        doc.value.ebookId = normalizeId(doc.value.ebookId);
    }

    // 调试：打印最终要提交的 payload
    console.log('handleModalOk submitting payload:', doc.value);

    axios.post("/doc/save", doc.value).then(resp => {
        //回去返回参数
        const data = resp.data;
        if (data.success) {
            modalLoading.value = false;//关闭等待
             modalVisible.value = false;//关闭对话框
            //重新加载列表
            handleQuery()
        } else {
            modalLoading.value = false;
            message.error(data.message || '保存失败');
        }
    }).catch((err) => {
        modalLoading.value = false;
        console.error('保存异常', err);
        message.error('提交失败，请检查控制台或网络请求');
    })
}

</script>
<style scoped></style>