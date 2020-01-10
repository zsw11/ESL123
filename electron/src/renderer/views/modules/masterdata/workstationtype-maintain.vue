<template>
  <el-card class="with-title">
    <div slot="header" class="clearfix">
      <div class="card-title">{{title}}</div>
      <div class="buttons">
        <el-button
          v-if="!$route.path.includes('details')"
          type="primary"
          @click="dataFormSubmit()"
        >保 存</el-button>
        <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取 消</el-button>
        <el-button v-if="$route.path.includes('details')" @click="cancleFormSubmit">确 定</el-button>
      </div>
    </div>
    <el-form
      :disabled="$route.path.includes('details')"
      :rules="dataRules"
      ref="dataForm"
      :model="dataForm"
      label-position="right"
      :size="'mini'"
      label-width="100px"
    >
      <el-row :gutter="10">
        <el-col :span="10">
          <el-form-item :label="'工位类型名'" prop="name">
            <el-input v-model="dataForm.name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :span="22">
          <el-form-item style="display: block" :label="'备注'" prop="remark">
            <el-input type="textarea" :rows="6" placeholder="请输入内容" v-model="dataForm.remark"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-card
      class="with-title"
      style="box-shadow: none;border: none"
      v-if="!$route.path.includes('add')"
    >
      <div style="border-bottom: 1px solid #BBBBBB;width: 600px;margin-bottom: 20px">
        <span class="tableHeader">工位类型结构</span>
        <el-button
          @click="show"
          type="primary"
          style="float: right"
          v-if="!$route.path.includes('details')"
        >新增</el-button>
      </div>
      <el-tree
        style="width: 600px"
        :data="data"
        node-key="dataForm.id"
        default-expand-all
        :expand-on-click-node="false"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ node.label }}</span>
          <span>
            <el-button
              v-if="$route.path.includes('edit')"
              class="delete"
              type="text"
              size="mini"
              @click="() => remove(node, data)"
            >删除</el-button>
          </span>
        </span>
      </el-tree>
      <el-dialog
        custom-class="worktype-dialog"
        width="40%"
        title="新增工位类型结构"
        :visible.sync="addReal"
        v-if="addReal"
      >
        <el-form ref="dialogForm" :model="addForm" :rules="dialogRules">
          <!--          <el-form-item :label="'父工位'" prop="parent">-->
          <!--            <keyword-search-->
          <!--              v-model="addForm.parent"-->
          <!--              -->
          <!--              :searchApi="this.listWorkstationTypeNode"-->
          <!--              :allowEmpty=true clearable>-->
          <!--            </keyword-search>-->
          <!--          </el-form-item>-->

          <el-form-item :label="'父工位'" prop="parent">
            <el-select v-model="addForm.parent" filterable placeholder="请选择" clearable>
              <el-option v-for="item in node" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item :label="'名称'" prop="name">
            <el-input v-model="addForm.name" clearable></el-input>
          </el-form-item>

          <el-form-item :label="'备注'" prop="remark">
            <el-input
              type="textarea"
              style="width:350px"
              :rows="6"
              placeholder="请输入内容"
              v-model="addForm.remark"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addReal = false">取 消</el-button>
          <el-button type="primary" @click="addNode">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
    <span class="dialog-footer">
      <el-button
        v-if="!$route.path.includes('details')"
        type="primary"
        @click="dataFormSubmit()"
      >保 存</el-button>
      <el-button v-if="!$route.path.includes('details')" @click="cancleFormSubmit">取 消</el-button>
      <el-button v-if="$route.path.includes('details')" @click="cancleFormSubmit">确 定</el-button>
    </span>
  </el-card>
</template>

<script>
import { pick } from "lodash";
import {
  fetchWorkstationType,
  createWorkstationType,
  updateWorkstationType,
  listWorkstationType
} from "@/api/workstationType";
import {
  fetchTypeNode,
  createWorkstationTypeNode,
  listWorkstationTypeNode,
  deleteWorkstationTypeNode
} from "@/api/workstationTypeNode";
export default {
  name: "editWorkstationType",
  data() {
    return {
      addForm: {
        name: null,
        remark: null,
        parent: null
      },
      node: null, //工位节点
      listWorkstationTypeNode,
      parentNode: null,
      addReal: false, // 新增页面显示
      data: [],
      defaultProps: {
        children: "children",
        label: "label"
      },
      title: null,
      flag: false,
      table: false,
      inited: false,
      dataForm: {
        id: 0,
        name: null,
        remark: null,
        createBy: null,
        createAt: null,
        updateBy: null,
        updateAt: null,
        deleteAt: null
      },
      listWorkstationType,
      dialogRules: {
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }]
      },
      dataRules: {
        name: [{ required: true, message: "名称不能为空", trigger: "blur" }],
        remark: [{ max: 512, message: "长度超过了512", trigger: "blur" }],
        createBy: [{ type: "number", message: "创建者ID需为数字值" }],

        updateBy: [{ type: "number", message: "更新者ID需为数字值" }]
      }
    };
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.fromFullPath = from.fullPath;
    });
  },
  created() {
    this.init();
  },
  activated() {
    if (
      this.dataForm.id &&
      parseInt(this.$route.params.id) !== this.dataForm.id
    ) {
      this.init();
    }
  },
  watch: {
    dataForm: {
      handler: function(val) {
        if (this.inited) {
          this.$store.dispatch("common/updateTabAttrs", {
            name: this.$route.name,
            changed: true
          });
        }
      },
      deep: true
    }
  },
  methods: {
    init() {
      this.title = this.$route.meta.title;
      this.$store.dispatch("common/updateTabAttrs", {
        name: this.$route.name,
        changed: false
      });
      this.inited = false;
      this.dataForm.id = parseInt(this.$route.params.id) || 0;
      if (this.dataForm.id) {
        fetchTypeNode(this.dataForm.id).then(page => {
          this.tree(page.data);
          this.node = page.data;
        });
        fetchWorkstationType(this.dataForm.id)
          .then(({ data }) => {
            Object.assign(
              this.dataForm,
              pick(data, [
                "name",
                "remark",
                "createBy",
                "createAt",
                "updateBy",
                "updateAt",
                "deleteAt"
              ])
            );
          })
          .finally(() => {
            this.inited = true;
          });
      } else {
        this.inited = true;
      }
    },
    // 取消信息
    cancleFormSubmit() {
      this.$store.dispatch("common/closeActiveTab");
      this.$router.push({ name: "masterdata-workstationtype" });
      this.$destroy();
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          (this.dataForm.id
            ? updateWorkstationType(this.dataForm.id, this.dataForm)
            : createWorkstationType(this.dataForm)
          ).then(({ data }) => {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: this.cancleFormSubmit
            });
          });
        }
      });
    },
    // 树的操作
    remove(node, data) {
      let arr = [data.id];
      this.$confirm("此操作将删除数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteWorkstationTypeNode(arr).then(code => {
          if (code) {
            this.init();
            this.$notify({
              title: "成功",
              message: "删除成功",
              type: "success",
              duration: 2000
            });
          }
        });
      });
    },
    // 新增节点
    addNode() {
      this.$refs["dialogForm"].validate(valid => {
        if (valid) {
          let data = {
            workstationTypeId: this.dataForm.id,
            parentId: this.addForm.parent,
            name: this.addForm.name,
            remark: this.addForm.remark
          };
          createWorkstationTypeNode(data).then(status => {
            if (status) {
              this.addReal = false;
              this.init();
              this.$notify({
                title: "成功",
                message: "添加关系成功",
                type: "success",
                duration: 2000
              });
            }
          });
        }
      });
    },
    // 处理树的数据
    tree(list) {
      let data = JSON.parse(JSON.stringify(list).replace(/name/g, "label"));
      let id = [];
      let parents = [];
      data.forEach(item => {
        id.push(item.id);
      });
      data.forEach(item => {
        if (!id.includes(item.parentId)) {
          parents.push(item);
        }
      });
      let children = data.filter(
        value => value.parentId !== "undefined" && value.parentId != null
      );
      let translator = (parents, children) => {
        parents.forEach(parent => {
          children.forEach((child, index) => {
            if (child.parentId === parent.id) {
              let temp = JSON.parse(JSON.stringify(children));
              temp.splice(index, 1);
              translator([child], temp);
              typeof parent.children !== "undefined"
                ? parent.children.push(child)
                : (parent.children = [child]);
            }
          });
        });
      };
      translator(parents, children);
      this.data = parents;
    },
    // show
    show() {
      this.addReal = true;
      this.addForm.parent = null;
      this.addForm.name = null;
      this.addForm.remark = null;
    }
  }
};
</script>
<style lang="scss">
.table-box {
  box-shadow: none;
  border: none;
}
.tableHeader {
  display: inline-block;
  width: 90px;
  height: 30px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  background-color: #1989fa;
  line-height: 30px;
  text-align: center;
  font-size: 13px;
  color: white;
}
.table {
  width: 600px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.delete {
  color: orangered;
}
.worktype-dialog {
  min-width: 580px;
  .el-form-item {
    display: block;
    .el-input {
      width: 350px;
    }
  }
}
</style>
