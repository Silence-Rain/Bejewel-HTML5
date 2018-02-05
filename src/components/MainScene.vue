<template>
	<div id="scene">
		<div class="row" v-for="row in matrix">
			<div class="gem" :x="item.x" :y="item.y" :style="{backgroundColor: item.color}" :class="{selected: item.isSelected}" @click="selectGem" v-for="item in row"></div>
		</div>
	</div>
</template>

<script>
	export default {
		name: "MainScene",
		data () {
			return {
				matrix: [],																																//宝石矩阵
				colors: ["#f00", "#0f0", "#00f", "#ff8c00", "#ffd700", "#800080", "#fff"],//宝石颜色枚举
				selected: false,																													//当前是否有宝石被选中
				first: {},																																//第一次选中的宝石坐标
				second: {}																																//第二次选中的宝石坐标
			}
		},

		//初始化，随机填充宝石矩阵（不能出现可消除的情况）
		created () {
			for (var i = 0; i < 8; i++) {
				var temp = []
				for (var j = 0; j < 8; j++) {
					var obj = {}
					obj.val = Math.ceil(Math.random() * 7)		//宝石的值（即颜色）
					obj.color = this.colors[obj.val - 1]			//宝石颜色
					obj.isSelected = false										//宝石被选中的状态
					obj.x = i 																//当前宝石横坐标
					obj.y = j																	//当前宝石纵坐标

					temp.push(obj)
				}
				this.matrix.push(temp)
			}

			//TODO：排除可消除的情况
		},

		methods: {
			//选中宝石
			//若是第一次选中，记录选中坐标并改变其选中状态
			//若是第二次选中，先检查能否交换
			//		若能交换，检查能否消除
			//				若能消除则消除，不能消除返回未交换状态
			//		若不能交换，取消对第一次选中的记录，并将当前（第二次）选中的结果记为第一次
			selectGem (event) {
				if (!this.selected) {
					this.selected = true
					this.first.x = event.target.attributes.x.value
					this.first.y = event.target.attributes.y.value
					this.matrix[this.first.x][this.first.y].isSelected = true
				}
				else {
					this.second.x = event.target.attributes.x.value
					this.second.y = event.target.attributes.y.value
					this.matrix[this.second.x][this.second.y].isSelected = true

					if (this.checkGemConnection(this.first, this.second)) {
						this.swap(this.first, this.second)
						console.log(this.first, this.second)

						if (this.checkGemElimination()) {
							this.eliminate()
						}
						else {
							this.swap(this.first, this.second)
						}

						this.matrix[this.first.x][this.first.y].isSelected = false
						this.matrix[this.second.x][this.second.y].isSelected = false
						this.selected = false
					}
					else {
						this.matrix[this.first.x][this.first.y].isSelected = false
						this.first.x = event.target.attributes.x.value
						this.first.y = event.target.attributes.y.value
					}
				}
			},

			//检查选中的两个宝石能否交换（必须相邻）
			checkGemConnection (first, second) {
				if ((Math.abs(first.x - second.x) == 1 && Math.abs(first.y - second.y) == 0) 
					|| (Math.abs(first.y - second.y) == 1 && Math.abs(first.x - second.x) == 0)) {
					return true
				}
				else {
					return false
				}
			},

			//交换矩阵中选中的两个宝石位置
			swap (first, second) {
				var temp

				temp = this.matrix[first.x][first.y].x
				this.matrix[first.x][first.y].x = this.matrix[second.x][second.y].x
				this.matrix[second.x][second.y].x = temp

				temp = this.matrix[first.x][first.y].y
				this.matrix[first.x][first.y].y = this.matrix[second.x][second.y].y
				this.matrix[second.x][second.y].y = temp

				temp = this.matrix[first.x][first.y]
				this.matrix[first.x][first.y] = this.matrix[second.x][second.y]
				this.matrix[second.x][second.y] = temp
			},

			//检查交换后状态是否可以消除（5*5邻域内判断）
			//先判断是否存在3个相连，若存在，沿该方向继续寻找更多的相连匹配
			checkGemElimination () {
				return true
			},

			//消除3个以上连着的宝石，并补充矩阵
			eliminate () {

			}
		}
	}
</script>

<style scoped>
.row {
	display: flex;
	flex-direction: row;
}

.gem {
	width: 80px;
	height: 80px;
	line-height: 80px;
	text-align: center;
	border: 1px solid #aaa;
}

.selected {
	border: 2px solid #000;
}
</style>