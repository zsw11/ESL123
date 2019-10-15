<template>
  <el-steps :active="active" finish-status="wait" process-status="finish" simple>
    <el-step title="接机" icon="el-icon-edit">
       <icon-svg name="01" style=" font-size: 24px;margin-top: 10px;" slot="icon"></icon-svg>
    </el-step>
    <el-step title="指派" icon="el-icon-edit">
      <icon-svg name="02" style=" font-size: 24px;margin-top: 10px;"  slot="icon"></icon-svg>
    </el-step>
    <el-step title="检测/报价" v-if="orderType==='order'" icon="el-icon-edit">
     <icon-svg name="03" style=" font-size: 24px;margin-top: 10px;"  slot="icon"></icon-svg>
    </el-step>
    <el-step title="维修" v-if="orderType==='order'" icon="el-icon-edit">
       <icon-svg name="04" style=" font-size: 24px;margin-top: 10px;"  slot="icon"></icon-svg>
    </el-step>
    <el-step title="待发" v-if="orderType==='order'" icon="el-icon-edit">
       <icon-svg name="05" style=" font-size: 24px;margin-top: 10px;"  slot="icon"></icon-svg>
    </el-step>
    <el-step title="发货" v-if="orderType==='order'" icon="el-icon-edit">
       <icon-svg name="06" style=" font-size: 24px;margin-top: 10px;"  slot="icon"></icon-svg>
    </el-step>
  </el-steps>
</template>

<script>
  export default {
    name: 'steps',
    props: {
      active: {
        type: Number
      },
      orderType: {
        type: String
      }
    }
  }
</script>