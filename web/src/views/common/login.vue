<template>
  <div class="site-wrapper site-page--login">
<!--    <div class="leftb"><img src="../../../static/img/rtop.png" alt=""></div>-->
    <div class="main clearfix">
      <div class="login-main">
        <div class="title">
          <h1 class="login-title">APO登陆</h1>
          <span class="title-eng">APO Login</span>
        </div>
        <el-form class="from" :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
          <el-form-item prop="username">
            <el-input class="username" v-model="dataForm.username" placeholder="帐号" size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input class="password" v-model="dataForm.password" type="password" placeholder="密码" size="medium"></el-input>
          </el-form-item>
          <span class="toggleLogin">本地用户登录</span>
          <el-form-item  class="identify" prop="jigsawVerify">
            <verify-slide @success="dataForm.jigsawVerify = true" @failure="dataForm.jigsawVerify = undefined"/>
          </el-form-item>
          <el-form-item>
            <el-button  class="login-btn-submit"  @click="dataFormSubmit()" size="large" :loading="logining">立即登录</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="mainRight">
        <span>标准时间分析系统
          <p>MOST</p>
        </span>

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
  .leftb{
    position: absolute;
    width: 300px;
    height: 350px;
    left: 0;
    bottom: 0;
    z-index: 0;
    img{
      width: 100%;
      height: 100%;
    }
  }
  .main {
    z-index: 999;
    width: 1080px;
    margin:0 auto;
    margin-top: 58px;
  }
  .clearfix {
    display: block;
    content: "";
    clear: both;
  }
  .login-main {
    font-family: SimHei;
    float: left;
    height: 521px;
    width: 400px;
    background-color: #fff;
    border-top-left-radius: 20px;
    border-bottom-left-radius: 20px;
  }
  .title{
    margin-top: 150px;
    margin-left: 50px;
  }
  .login-title {
    margin: 0;
    font-size: 40px;
    color: #1F297E;
  }
  .from{
    margin-top: 25px;
    margin-left: 50px;
  }

  .username > input,.password >input {
    width: 250px;
    border-radius: 0;
    border-left: none;
    border-top: none;
    border-right: none;
  }
  .title-eng{
    margin-top: 5px;
    color: #BDBDBD;
    font-size: 18px;
  }

  .login-btn-submit {
    width: 180px;
    height: 40px;
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
    padding: 0;
    margin: 0;
    float: left;
    height: 521px;
    width: 680px;
    background-image: url("../../../static/img/main.jpg");
    background-size: 100% 100%;
    background-repeat: no-repeat;
    border-top-right-radius: 20px;
    border-bottom-right-radius: 20px;
    color: white;
    span{
      padding: 0;
      display: block;
      font-size: 50px;
      margin-top: 150px;
      margin-left: 100px;
      p{
        margin: 0;
        font-size:30px ;
        font-weight: lighter;
      }
    }
  }

  .mainRight>img{
    width: 700px;
    height: 521px;
  }
  .toggleLogin{
    cursor: pointer;
    color: #1F297E;
  }
  .identify{
    margin-top: 10px;
    width: 250px;
  }
  @media only screen and (min-width: 0px) and (max-width: 640px){
    .mainRight{
      display: none;
    }
  }


</style>
