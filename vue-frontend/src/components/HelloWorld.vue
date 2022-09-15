<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <el-row>
      <el-button>default</el-button>
      <el-button type="primary">primary</el-button>
      <el-button type="success">success</el-button>
      <el-button type="info">info</el-button>
      <el-button type="warning">warning</el-button>
      <el-button type="danger">danger</el-button>
    </el-row>
    <el-table
      :data="userList"
      style="width: 100%">
      <el-table-column
        prop="id"
        label="id"
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="name"
        width="180">
      </el-table-column>
      <el-table-column
        prop="address"
        label="address">
      </el-table-column>
      <el-table-column
        prop="email"
        label="email">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getAllUsers } from '../services/user'
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      userList: []
    }
  },

  methods: {
    getAllUsers() {
      getAllUsers().then(response => {
        console.log(response)
        this.userList = response
      })
    }
  },
  mounted() {
    this.getAllUsers();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
