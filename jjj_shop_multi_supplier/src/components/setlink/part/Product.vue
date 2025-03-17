<template>
  <div class="marketing-box">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="详情" name="detail"></el-tab-pane>
    </el-tabs>

    <div class="product-list" v-if="activeTab == 'detail'" v-loading="loading">
      <!--搜索表单-->
      <div class="common-seach-wrap">
        <el-form
          size="small"
          :inline="true"
          :model="searchForm"
          class="demo-form-inline"
        >
          <el-form-item label="商品名称"
            ><el-input
              size="small"
              v-model="searchForm.productName"
              placeholder="请输入商品名称"
            ></el-input
          ></el-form-item>
          <el-form-item>
            <el-button size="small" icon="Search" @click="onSubmit"
              >查询</el-button
            >
          </el-form-item>
        </el-form>
      </div>

      <!--内容-->
      <div class="product-content">
        <div class="table-wrap setlink-product-table">
          <el-table size="small" :data="tableData" border style="width: 100%">
            <el-table-column prop="productName" label="产品">
              <template #default="scope">
                <div class="product-info">
                  <div class="pic">
                    <img v-img-url="scope.row.imagePath" alt="" />
                  </div>
                  <div class="info">
                    <div class="name text-ellipsis">
                      {{ scope.row.productName }}
                    </div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="productPrice" label="价格" width="100">
              <template #default="scope">
                <span class="red">{{ scope.row.productPrice }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template #default="scope">
                <el-button size="small" @click="changeFunc(scope.row)"
                  >选择</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <!--分页-->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
          :current-page="curPage"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="totalDataNumber"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import PorductApi from "@/api/product.js";
export default {
  data() {
    return {
      /*是否正在加载*/
      loading: true,
      /*tab切换选择中值*/
      activeTab: "detail",
      /*产品类别列表*/
      categoryList: [],
      /*当前选中*/
      categoryActive: [],
      /*搜索表单对象*/
      searchForm: {
        productName: "",
      },
      /*产品数据表*/
      tableData: [],
      /*一页多少条*/
      pageSize: 5,
      /*一共多少条数据*/
      totalDataNumber: 0,
      /*当前是第几页*/
      curPage: 1,
      /*选中的页面值*/
      activePage: null,
    };
  },
  created() {
    /*获取产品类别*/
    this.getData();
  },
  watch: {
    activeTab: function (n, o) {
      if (n != o) {
        this.tableData = [];
        if (n == "detail") {
          this.getData();
        }
      }
    },
  },
  methods: {
    /*选择第几页*/
    handleCurrentChange(val) {
      let self = this;
      self.curPage = val;
      self.getData();
    },

    /*每页多少条*/
    handleSizeChange(val) {
      this.pageSize = val;
      this.curPage = 0;
      this.getData();
    },

    /*获取列表*/
    getData() {
      let self = this;
      self.loading = true;
      let Params = {};
      Params.pageIndex = self.curPage;
      Params.pageSize = self.pageSize;
      Params.productName = self.searchForm.productName;
      Params.type = "sell";
      PorductApi.productList(Params, true)
        .then((res) => {
          self.loading = false;
          self.tableData = res.data.productList.records;
          self.totalDataNumber = res.data.productList.total;
          if (self.curPage == 1 && self.tableData.length > 0) {
            self.changeFunc(self.tableData[0]);
          }
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*搜索查询*/
    onSubmit() {
      this.curPage = 0;
      this.getData();
    },

    /*选中的值*/
    changeFunc(e) {
      let obj = {};
      if (this.activeTab == "detail") {
        obj.name = e.productName;
        obj.url = "pages/product/detail/detail?productId=" + e.productId;
        obj.type = "商品详情";
      }

      this.$emit("changeData", obj);
    },
  },
};
</script>
<style scoped>
.table-wrap.setlink-product-table .product-info .pic {
  width: 20px;
  height: 20px;
  background: #ffffff;
}

.table-wrap.setlink-product-table .product-info .pic img {
  width: 20px;
  height: 20px;
}

.marketing-box .el-tree-node__content {
  height: 30px;
}

.marketing-box .el-tree-node__content {
  margin-bottom: 10px;
}
</style>
