import { 
    UilSun, 
    UilCloud,
    UilClouds,
    UilCloudRain,
    UilCloudShowersHeavy,
    UilCloudHail,
    UilCloudSun,
    UilSnowflake,
    UilCloudWind,
    UilWind,
    UilWindSun,
    UilThunderstorm,
    UilExclamationTriangle
    } from '@iconscout/react-unicons';
import { React, useState, useEffect } from 'react';

    const weatherTypes = {
        isCloud: [802],
        isHeavyCloud: [803, 804],
        isSun: [800],
        isRain: [300, 301, 302, 310, 311, 312, 313, 314, 321, 500, 501, 511],
        isHeavyRain: [502, 503, 504, 520, 521, 522, 531],
        isHail: [],
        isSnow: [600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622],
        isThunderstorm: [200, 201, 202, 210, 211, 212, 221, 230, 231, 232],
        isCloudSun: [801],
        isWarning: [701, 711, 721, 731, 741, 751, 761, 762, 771, 781]
    }; 

    const iconSize = 200;

    const weatherIcons = {
        isCloud: <UilCloud size={iconSize}/>,
        isHeavyCloud: <UilClouds size={iconSize}/>,
        isSun: <UilSun size={iconSize}/>,
        isRain: <UilCloudRain size={iconSize}/>,
        isHeavyRain: <UilCloudShowersHeavy size={iconSize}/>,
        isHail: <UilCloudHail size={iconSize}/>,
        isSnow: <UilSnowflake size={iconSize}/>,
        isThunderstorm: <UilThunderstorm size={iconSize}/>,
        isWind: <UilWind size={iconSize}/>,
        isWindSun: <UilWindSun size={iconSize}/>,
        isWindCloud: <UilCloudWind size={iconSize}/>,
        isCloudSun: <UilCloudSun size={iconSize}/>,
        isWarning: <UilExclamationTriangle size={iconSize}/>
    };

    const WeatherIcon =  ({currentApiId}) => {
        const [currentWeatherIcon, setCurrentWeatherIcon] = useState('isSun');

        useEffect(() => {
            const apiIdToWeatherType = apiId =>
                Object.entries(weatherTypes).reduce(
                    (currentWeatherType, [weatherType, apiIds]) =>
                        apiIds.includes(Number(apiId))
                            ? weatherType
                            : currentWeatherType,
                            '',
                );
            const currentWeatherIcon = apiIdToWeatherType(currentApiId);

            setCurrentWeatherIcon(currentWeatherIcon);

        }, [currentApiId]);
        
    
      return (
        <div className='weatherIcon'>
            {weatherIcons[currentWeatherIcon]}
        </div>
      )
    }
    
    export default WeatherIcon;