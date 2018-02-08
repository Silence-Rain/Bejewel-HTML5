<template>
  <div id="app">
    <div class="left">
      <Scoreboard :score="score"/>
      <CtrlPanel :score="score"/>
    </div>
    <div class="right">
      <MainScene @inc="inc"/>
    </div>
  </div>
</template>

<script>
import MainScene from './components/MainScene'
import Scoreboard from './components/Scoreboard'
import CtrlPanel from './components/CtrlPanel'

export default {
  name: 'App',
  components: {
    MainScene,
    Scoreboard,
    CtrlPanel
  },

  data () {
    return {
      score: 0
    }
  }, 

  created () {
    if (localStorage.score) {
      this.score = Number(localStorage.score)
    }
    else {
      this.score = 0
    }
  },

  methods: {
    inc (msg) {
      this.score += msg * 10
    }
  }
}
</script>

<style scoped>
#app {
  display: flex;
  flex-direction: row;
  font-family: "Microsoft Yahei UI";
}

.left {
  width: 300px;
  margin: 0 20px;
  display: flex;
  flex-direction: column;
}

.right {
  width: 700px;
  margin: 0 20px;
}
</style>
