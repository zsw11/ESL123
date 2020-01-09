<template>
  <div>
    <div class="site-wrapper site-page--login">
      <div class="main-wrapper">
        <div class="main">
          <div class="login-main">
            <img src="@/assets/img/favicon.png" alt />
            <div class="title">
              <h1 class="login-title">{{modesMap[currentMode].name}}登录</h1>
              <p class="title-eng">{{modesMap[currentMode].engName}} Login</p>
            </div>
            <!-- 登录框 -->
            <el-form
              class="from"
              :model="dataForm"
              :rules="dataRule"
              ref="dataForm"
              @keyup.enter.native="dataFormSubmit()"
              status-icon>
              <el-form-item prop="username">
                <el-input
                  class="username"
                  v-model="dataForm.username"
                  placeholder="帐号"
                  size="medium">
                </el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input
                  class="password"
                  v-model="dataForm.password"
                  :type="pwType[pw].type"
                  placeholder="密码"
                  size="medium">
                </el-input>
              </el-form-item>
              <span class="toggle-pw" @click="togglePwShow()">{{pwType[pw].title}}</span>
              <span class="toggle-login" @click="toggleLogin()">{{modesMap[currentMode].otherName}}登录</span>
              <el-form-item>
                <el-button
                  id="login-btn-submit"
                  @click="dataFormSubmit()"
                  size="large"
                  :loading="logining"
                >立即登录</el-button>
              </el-form-item>
            </el-form>
          </div>
          <!-- 系统名称 -->
          <div class="main-right">
            <h2>标准时间分析管理系统</h2>
            <span>Standard time analysis management system</span>
          </div>
        </div>
      </div>
    </div>
    <div class="footer">@ Copyright Seiko Epson Corporation 2008</div>
  </div>
</template>

<script>
import { keyBy } from 'lodash'
import VerifySlide from "@/components/verify-slide";

const modesMap = {
  apo: {
    name: 'APO',
    engName: 'APO',
    otherName: '本地用户'
  },
  local: {
    name: '本地用户',
    engName: 'Local',
    otherName: 'APO'
  }
}

const pwType = {
  show: {
    type: null,
    title: '隐藏密码'
  },
  hide: {
    type: 'password',
    title: '显示密码'
  }
}
export default {
  components: {
    VerifySlide
  },
  data() {
    return {
      pw:'hide',
      logining: false,
      currentMode: 'apo',
      dataForm: {
        username: null,
        password: null,
        apo: null,
        uuid: "",
        jigsawVerify: undefined
      },
      dataRule: {
        username: [
          { required: true, message: "帐号不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        jigsawVerify: [{ required: true, message: "请拖动拼图进行校验" }]
      },
      captchaPath: "",
      modesMap,
      pwType
    };
  },
  methods: {
    // 提交表单
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        this.dataForm.apo = this.currentMode === "apo";
        console.log(this.dataForm)
        if (valid) {
          this.logining = true;
          this.$store
            .dispatch("user/login", this.dataForm)
            .then(() => {
              this.$router.replace({ name: "home" });
            })
            .finally(() => {
              this.logining = false;
            });
        }
      });
    },
    // 切换登录
    toggleLogin() {
      this.dataForm.username = null;
      this.dataForm.password = null;
      this.currentMode = this.currentMode === 'apo' ? 'local' : 'apo';
    },
    // 切换密码显示
    togglePwShow() {
      this.pw = this.pw === 'show' ? 'hide' : 'show'
    }
  }
};
</script>


<style lang="scss">
.site-wrapper.site-page--login {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: #f6f5fb;
}
.main-wrapper {
  padding-top: 40%;
  position: relative;
  top: 13%;
}
.main {
  -moz-user-select: none;
  -khtml-user-select: none;
  user-select: none;
  position: absolute;
  top: 0;
  left: 11%;
  width: 78%;
  height: 95%;
  min-width: 890px;
  min-height: 450px;
  background-size: cover;
  box-sizing: border-box;
  border-radius: 30px;
}

.leftB {
  position: absolute;
  width: 23%;
  height: 58%;
  bottom: 0;
  z-index: 0;
  background-image: url("~@/assets/img/rtop.png");
  background-size: 100% 100%;
}
.rightT {
  position: absolute;
  width: 23%;
  height: 56.6%;
  top: 0;
  right: 0;
  z-index: 0;
  background-image: url("~@/assets/img/lbottom.png");
  background-size: 100% 100%;
}

.login-main {
  position: absolute;
  display: inline-block;
  font-family: SimHei;
  padding-top: 20px;
  padding-left: 45px;
  padding-right: 30px;
  width: 38%;
  height: 100%;
  background-color: #ffffff;
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
  img {
    width: 45%;
    height: 13%;
  }
}
.title {
  margin-top: 25%;
  padding-left: 14px;
}
.login-title {
  margin-top: 10%;
  font-size: 40px;
  color: #1f297e;
}
.title-eng {
  margin-top: -20px;
  color: #bdbdbd;
  font-size: 18px;
  padding-left: 5px;
}

.from {
  padding-left: 14px;
  margin-top: 22px;
}

.username > input,
.password > input {
  border-radius: 0;
  width: 90%;
  border-left: none;
  border-top: none;
  border-right: none;
}

#login-btn-submit {
  margin-top: 7%;
  width: 50%;
  height: 12%;
  border-radius: 90px;
  background-color: #172379;
  color: white;
  font-weight: bold;
  font-size: 16px;
}
#login-btn-submit:hover {
  background-color: #172379;
}
#login-btn-submit:focus {
  background-color: #172379;
}
.main-right {
  position: absolute;
  display: inline-block;
  height: 100%;
  left: 38%;
  width: 62%;
  background-image: url("~@/assets/img/main.jpg");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  border-top-right-radius: 30px;
  border-bottom-right-radius: 30px;
  color: #172379;
  h2 {
    border-radius: 30px;
    margin-top: 10%;
    margin-left: 15%;
    font-size: 50px;
    font-weight: lighter;
    margin-bottom: 0;
  }
  span {
    margin-top: -25px;
    margin-left: 15%;
    font-size: 25px;
    // font-family: Dotum;
    // font-weight: lighter;
  }
}

.toggle-pw {
  cursor: pointer;
}

.toggle-login {
  margin-top: 5%;
  cursor: pointer;
  color: #1f297e;
  height: 20px;
  display: block;
}
.identify {
  margin-top: 10px;
  width: 250px;
}
  .footer{
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 20px;
    line-height: 20px;
    text-align: center;
    background-color:#000000;
    color: #FFFFFF;
  }
</style>
