<template>
  <div class="site-wrapper site-page--login">
    <div class="site-content__wrapper">
      <div class="site-content">
        <div class="site-box">
          <div class="brand_logo"></div>
          <div class="brand-info">
            <h2 class="brand-info__text">APJ <span>Security</span></h2>
          </div>
          <div class="login-main">
            <h3 class="login-title">欢迎登陆</h3>
            <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
              <el-form-item prop="username">
                <el-input v-model="dataForm.username" placeholder="帐号" size="medium"></el-input>
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="dataForm.password" type="password" placeholder="密码" size="medium"></el-input>
              </el-form-item>
              <el-form-item prop="jigsawVerify">
                <verify-slide @success="dataForm.jigsawVerify = true" @failure="dataForm.jigsawVerify = undefined"/>
              </el-form-item>
              <el-form-item>
                <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()" size="large" :loading="logining">登录</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import VerifySlide from '@/components/verify-slide'

  export default {
    components: {
      VerifySlide
    },
    data () {
      return {
        logining: false,
        dataForm: {
          username: 'admin',
          password: 'admin',
          uuid: '',
          jigsawVerify: undefined
        },
        dataRule: {
          username: [
            { required: true, message: '帐号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          jigsawVerify: [
            { required: true, message: '请拖动拼图进行校验' }
          ]
        },
        captchaPath: ''
      }
    },
    methods: {
      // 提交表单
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.logining = true
            this.$store.dispatch('user/login', this.dataForm)
            .then(() => {
              this.$router.replace({ name: 'home' })
            }).finally(() => {
              this.logining = false
            })
          }
        })
      }
    }
  }
</script>


<style lang="scss">
  .site-wrapper.site-page--login {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: hidden;
    &:before {
      position: fixed;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100vw;
      height: 52vw;
      content: "";
      background-image: url(~@/assets/img/login_bg.jpg);
      background-size: 100% 100%;
    }
    .site-content__wrapper {
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      left: 0;
      padding: 0;
      margin: 0;
      overflow-x: hidden;
      overflow-y: auto;
      background-color: transparent;
    }
    .site-content {
      position: relative;
      min-height: 100%;
    }
    .site-box {
      position: absolute;
      left: 20px;
      top: 20px;
      right: 20px;
      bottom: 20px;
      background-color: rgba(255, 255, 255, 0.1);
      background-image: url(~@/assets/img/login_earth.png);
      background-repeat: no-repeat;
      background-size: auto 70%;
      background-position: 10% 70%;
    }
    .brand_logo {
      margin-left: 20px;
      margin-top: 20px;
      width: 400px;
      height: 40px;
      background-size: cover;
      background-image: url(~@/assets/img/login_logo.png);
    }
    .brand-info {
      position: absolute;
      right: 420px;
      bottom: 20px;
      color: #fff;
    }
    .brand-info__text {
      margin:  0 0 22px 0;
      font-size: 36px;
      span {
        font-weight: initial
      }
    }
    .login-main {
      position: absolute;
      top: 0;
      right: 0;
      bottom: 0;
      padding: 100px 60px;
      width: 400px;
      background-color: #fff;
    }
    .login-title {
      font-size: 16px;
      text-align: center;
      margin: 20px auto;

      &:after {
        content: "";
        display: block;
        position: absolute;
        width: 4em;
        left: 50%;
        margin-left: -2em;
        border-bottom: #1989FA solid 3px;
      }
    }
    .login-captcha {
      overflow: hidden;
      > img {
        width: 100%;
        cursor: pointer;
      }
    }
    .login-btn-submit {
      width: 100%;
    }
  }
</style>
