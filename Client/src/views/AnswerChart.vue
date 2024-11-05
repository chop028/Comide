<template>
  <div class="chart-box">
    <canvas ref="MyChart"/>
  </div>
</template>

<script>
import { Chart, registerables } from 'chart.js'
Chart.register(...registerables)

export default {
  data() {
    return {
      type: 'bar',
      data: {
        labels: ['1번', '2번', '3번'],
        datasets: [{
          label: '문제별 오답률 (%)',
          data: [24, 12, 30],
          backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
          ],
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
            max: 100, // y 축의 최대값을 100으로 설정
            ticks: {
              stepSize: 20, // y 축 간격을 20으로 설정 (옵션)
              callback: function(value) {
                return value + '%'; // y 축 레이블에 '%' 추가
              }
            }
          }
        }
      }
    }
  },
  mounted() {
    this.createChart()
  },
  methods: {
    createChart() {
      new Chart(this.$refs.MyChart, {
        type: 'bar',
        data: this.data,
        options: this.options
      })
    }
  }
}
</script>

<style scoped>
.chart-box {
  margin-right: 100px;
  margin-top: 130px;
}
</style>