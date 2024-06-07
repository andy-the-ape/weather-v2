import React, { useState, useEffect } from 'react';
import TopButtons from '../components/TopButtons/TopButtons';
import axios from 'axios';
import LineChart from '../components/LineChart/LineChart';

function History() {
  const [weatherRecords, setWeatherRecords] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [
      {
        label: 'Temperatur',
        data: [],
        backgroundColor: 'rgb(255, 99, 132)',
        borderColor: 'rgb(255, 255, 255)',
        borderWidth: 2,
      },
    ]
  });

  useEffect(() => {
    const baseURL = 'http://localhost:8080';
    axios.get(`${baseURL}/api/v1/weather/history/48`)
      .then((response) => {
        setWeatherRecords(response.data);

        // Update chartData based on fetched weather records
        const labels = response.data.map((record) => {
          const dateTime = new Date(record.date + 'T' + record.time); // Combine date and time
          const formattedTime = dateTime.toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });
          return `${record.date} ${formattedTime}`;
        });

        const temperatureData = response.data.map((record) => record.temperature);
        const windSpeedData = response.data.map((record) => record.windSpeed);
        const humidityData = response.data.map((record) => record.humidity);
        setChartData({
          labels: labels,
          datasets: [
            {
              label: 'Temperatur (°C)',
              data: temperatureData,
              borderColor: 'rgb(255, 99, 132)',
              backgroundColor: 'rgba(255, 99, 132, 0.5)',
              borderWidth: 3,
            },
            {
              label: 'Vindhastighed (km/t)',
              data: windSpeedData,
              borderColor: 'rgb(53, 162, 235)',
              backgroundColor: 'rgba(53, 162, 235, 0.5)',
              borderWidth: 3,
              hidden: true,
            },
            {
              label: 'Luftfugtighed (%)',
              data: humidityData,
              borderColor: 'rgb(112, 171, 56)',
              backgroundColor: 'rgba(112, 171, 56, 0.5)',
              borderWidth: 3,
              hidden: true,
            }
          ]
        });
        setLoading(false); // Update loading state
      })
      .catch(error => {
        setError(error);
        setLoading(false); // Update loading state even if there's an error
      });
  }, []);

  if (loading) return "Loading..."; // Display loading message while waiting for API response
  if (error) return `Error: ${error.message}`;
  if (!weatherRecords || weatherRecords.length === 0) return "No weather records!";

  return (
    <div className="app">
      <div className="container">
        <div className="topbuttons">
          <TopButtons />
        </div>
        <div className="chartContainer">
            <LineChart chartData={chartData}/>
        </div>
      </div>
    </div>
  );
}

export default History;
