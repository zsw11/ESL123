<template>
  <div>
  <div class="site-wrapper site-page--login">
    <div class="leftb"></div>
    <div class="right"></div>
      <div class="main-wrapper">
        <div class="main">
      <div class="login-main">
        <img src="../../../static/img/favicon.png" alt="">
        <div class="title">
          <h1 class="login-title">APO登录</h1>
          <p class="title-eng">APO Login</p>
        </div>
        <el-form class="from" :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
          <el-form-item prop="username">
            <el-input class="username" v-model="dataForm.username" placeholder="帐号" size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input class="password" v-model="dataForm.password" type="password" placeholder="密码" size="medium"></el-input>
          </el-form-item>
          <span class="toggleLogin">本地用户登录</span>
<!--          <el-form-item  class="identify" prop="jigsawVerify">-->
<!--            <verify-slide @success="dataForm.jigsawVerify = true" @failure="dataForm.jigsawVerify = undefined"/>-->
<!--          </el-form-item>-->
          <el-form-item>
            <el-button  class="login-btn-submit"  @click="dataFormSubmit()" size="large" :loading="logining">立即登录</el-button>
          </el-form-item>
        </el-form>
      </div><div class="mainRight">
          <h2>标准时间分析系统</h2>
          <span>MOST</span>
        </div>
      </div>
<!--        <span>标准时间分析系统-->
<!--          <p>MOST</p>-->
<!--        </span>-->
<!--      </div>-->
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
    background-color: #F6F5FB;
  }
  .main-wrapper{
    padding-top: 40%;
    position: relative;
    top: 10%;
  }
  .main{
    position: absolute;
    top:0;
    left: 11%;
    width: 78%;
    height:95%;
    background-size: cover;
    box-sizing: border-box;
    border-radius: 30px;
  }

  .leftb{
    position: absolute;
    width: 23%;
    height: 58%;
    bottom: 0;
    z-index: 0;
    background-image: url("../../../static/img/rtop.png");
    background-size: 100% 100%;
  }
  .right{
    position: absolute;
    width: 23%;
    height: 56.6%;
    top:0;
    right: 0;
    z-index: 0;
    background-image: url("../../../static/img/lbottom.png");
    background-size: 100% 100%;
  }

  .login-main {
    position: absolute;
    display: inline-block;
    font-family: SimHei;
    padding-top: 20px;
    padding-left: 45px;
    padding-right: 70px;
    width: 38%;
    height: 100%;
    background-color: #ffffff;
    border-top-left-radius: 30px;
    border-bottom-left-radius: 30px;
    img{
      width:45%;
      height: 11%;
    }
  }
  .title{
    margin-top: 30%;
    padding-left: 14px;
  }
  .login-title {
    margin-top: 10%;
    font-size: 40px;
    color: #1F297E;

  }
  .title-eng{
    margin-top: -15px;
    color: #BDBDBD;
    font-size: 18px;
  }

  .from{
    padding-left: 14px;
    margin-top: 22px;
  }

  .username > input,.password >input {
    border-radius: 0;
    border-left: none;
    border-top: none;
    border-right: none;
  }


  .login-btn-submit {
    margin-top: 10%;
    width: 70%;
    height: 12%;
    border-radius: 90px;
    background-color: #172379;
    color: white;
    font-weight: bold;
    font-size: 16px;
  }
  .login-btn-submit:hover{
    background-color: #172379;
  }
  .mainRight{
    position: absolute;
    display: inline-block;
    height: 100%;
    left: 38%;
    width: 62%;
    background-image: url("../../../static/img/main.jpg");
    background-size: 100% 100%;
    background-repeat: no-repeat;
    border-top-right-radius: 30px;
    border-bottom-right-radius: 30px;
    color: white;
    h2 {
      border-radius: 30px;
      margin-top:23%;
      margin-left:15%;
      font-size: 46px;
      font-weight:lighter;
      margin-bottom: 0;
    }
    span {
      margin-top: -25px;
      margin-left: 15%;
      font-size: 45px;
      font-weight: lighter;
    }
  }

  .toggleLogin{
    cursor: pointer;
    color: #1F297E;
  }
  .identify{
    margin-top: 10px;
    width: 250px;
  }



</style>
