import com.example.weatherapp.DataSource.ILocalDataSource
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.forcastmodel.Favorites
import com.example.weatherapp.forcastmodel.Forcast
import com.example.weatherapp.weathermodel.ExampleJson2KtKotlin
import kotlinx.coroutines.flow.*

class FakeLocal : ILocalDataSource {

    // MutableStateFlows to track changes to data
    private val _weathers = MutableStateFlow<List<ExampleJson2KtKotlin>>(emptyList())
    private val _favourites = MutableStateFlow<List<Favorites>>(emptyList())
    private val _forecast = MutableStateFlow<Forcast?>(null)
    private val _alerts = MutableStateFlow<List<MyAlerts>>(emptyList())

    override suspend fun insertForecast(forecast: Forcast) {
        _forecast.value = forecast
    }

    override fun getForecast(lat: Double, lon: Double, lang: Int): Flow<Forcast?> {
        // Simply emit the current forecast
        return _forecast.asStateFlow()
    }

    override fun getFavorite(): Flow<List<Favorites>> {
        // Expose favorites as read-only Flow
        return _favourites.asStateFlow()
    }

    override suspend fun insertWeather(weather: ExampleJson2KtKotlin): Long {
        // Add weather and emit the updated list
        _weathers.value = _weathers.value + weather
        return _weathers.value.indexOf(weather).toLong()
    }

    override fun getWeather(cityName: Int, e: Int): Flow<ExampleJson2KtKotlin?> {
        // Find the weather by cityName and id, and emit it
        return _weathers.map { it.find { weather -> weather.language == cityName && weather.id == e } }
    }

    override fun getWeather(lat: Double, lon: Double, lang: Int): Flow<ExampleJson2KtKotlin?> {
        // Find the weather by lat, lon, and language, and emit it
        return _weathers.map { it.find { weather -> weather.lat == lat && weather.lon == lon && weather.language == lang } }
    }

    override fun getAlerts(): Flow<List<MyAlerts>> {
        // Expose alerts as read-only Flow
        return _alerts.asStateFlow()
    }

    override suspend fun addAlert(alert: MyAlerts): Long {
        // Add alert and emit the updated list
        _alerts.value = _alerts.value + alert
        return _alerts.value.indexOf(alert).toLong()
    }

    override suspend fun deleteAlert(alert: MyAlerts) {
        // Remove alert and emit the updated list
        _alerts.value = _alerts.value - alert
    }

    override fun getAlert(id: Int): Flow<MyAlerts?> {
        // Find the alert by id and emit it
        return _alerts.map { it.find { alert -> alert.id == id } }
    }

    override suspend fun addFavorite(favorite: Favorites) {
        // Add favorite and emit the updated list
        _favourites.value = _favourites.value + favorite
    }

    override suspend fun deleteFavorite(favorite: Favorites) {
        // Remove favorite and emit the updated list
        _favourites.value = _favourites.value - favorite
        println("favourites.size  ${_favourites.value.size}")
    }
}
