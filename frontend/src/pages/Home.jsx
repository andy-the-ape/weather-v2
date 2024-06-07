import React from 'react';
import TopButtons from '../components/TopButtons/TopButtons';
import WeatherIcon from '../components/WeatherIcon/WeatherIcon';
import Wind from '../components/Wind/Wind';

function Home({ weatherRecord }) {

  return (
    <div className="app">
      <div className="container">
        <div className="topbuttons">
          <TopButtons />
        </div>
        <div className="top">
          <div className="location">
            {weatherRecord.location.name && <p>{weatherRecord.location.name}</p>}
          </div>
          <div className="date">
            {weatherRecord.date && <p>{weatherRecord.date}</p>}
          </div>
          <div className="temp">
            {weatherRecord.temperature && <h1>{weatherRecord.temperature.toFixed()}°C</h1>}
          </div>
          <div className="description">
            {weatherRecord.description && <p>{weatherRecord.description}</p>}
          </div>
        </div>
        <div className="mid">
          <div className="weathericon">
            <WeatherIcon currentApiId={weatherRecord.type.apiId}/>
          </div>
        </div>
        <div className="bottom">
          <div className="wind">
            {weatherRecord.windSpeed && (
              <>
                <p className='bold'>{weatherRecord.windSpeed.toFixed(1)} km/t</p>
                <p>Vindhastighed</p>
              </>
            )}
          </div>
          <div className="wind">
            <Wind degrees={weatherRecord.windDirection}/>
          </div>
          <div className="humidity">
            {weatherRecord.humidity && (
              <>
                <p className='bold'>{weatherRecord.humidity}%</p>
                <p>Luftfugtighed</p>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
