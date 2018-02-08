<template>
	<div id="score">
		<div class="title">Score</div>
		<div class="scoreboard">
			<div class="score-item" v-for="item in path">
				<img :src="item">
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "Scoreboard",

		props: {
			score: Number
		},

		computed: {
			path () {
				var ret = []
				var temp = this.score

				for (var i = 0; i < 4; i++) {
					let	base = Math.pow(10, 3 - i)
					let unit = Math.floor(temp / base)
					temp -= unit * base

					ret[i] = require("../assets/score" + unit + ".png")
				}

				return ret
			}
		}
	}
</script>

<style scoped>
#score {
	height: 200px;
}

.title {
	height: 60px;
	margin: 10px 20px;
	font-size: 30px;
	text-align: center;
	line-height: 60px;
}

.scoreboard {
	height: 100px;
	margin: 10px 20px;
	display: flex;
	flex-direction: row;
}

.score-item {
	width: 55px;
	height: 80px;
	margin: 0 5px;
	border-radius: 10px;
}

.score-item img {
	width: 55px;
	height: 80px;
}
</style>