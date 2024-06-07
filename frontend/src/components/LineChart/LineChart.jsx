import React from 'react';
import {Line} from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js/auto';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const options = {
  responsive: true,
  maintainAspectRatio: false,
  aspectRatio: 1,
  scales: {
    x: {
      title: {
        display: true,
        color: 'white'
      },
      ticks: {
        color: 'white'
      }
    },
    y: {
      title: {
        display: true,
        color: 'white'
      },
      ticks: {
        color: 'white'
      }
    }
  },
  plugins: {
    title: {
      display: true,
      text: 'Vejret de sidste 48 timer',
      font: {
        size: 20
      },
      color: 'white'
    },
    legend: {
      display: true,
      labels: {
        color: 'white',
        padding: 20
      }
    },
  },
}

const chartBackgroundPlugin = {
  id: 'chartBackgroundPlugin',
  beforeDatasetsDraw(chart, args, plugins) {
    const {ctx, chartArea: {top, left, width, height}} = chart;
    ctx.save();

    ctx.fillStyle = 'rgba(255, 255, 255, 0.3)';
    ctx.fillRect(left, top, width, height);
  }
}

function LineChart({chartData}) {
  return (
    <Line 
    data={chartData} 
    redraw={true}
    options={options} 
    plugins={[chartBackgroundPlugin]}/>
  )
}

export default LineChart