package net.inferno.hwbenchmarks.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.inferno.hwbenchmarks.model.BenchmarkModel
import org.jsoup.Jsoup

class RemoteDataSource : DataSource {
    override suspend fun getSingleCPUsData() = withContext(Dispatchers.IO) {
        val doc = Jsoup.connect(cpus_url).get()
        val tables = doc
            .getElementById("cputable")!!
            .getElementsByTag("tbody")[0]
            .getElementsByTag("tr")
            .removeClass("repeated-header odd")

        val benchmarks = tables.map {
            val td = it.getElementsByTag("td")
            BenchmarkModel(
                td[0].getElementsByTag("a")[0].text(),
                td[0].getElementsByTag("a")[0].attr("href"),
                td[1].text().replace(",", "").toInt(),
                td[2].text().replace(",", "").toInt()
            )
        }

        benchmarks
    }

    override suspend fun getGPUsData() = withContext(Dispatchers.IO) {
        val doc = Jsoup.connect(gpus_url).get()

        val tables = doc
            .getElementById("cputable")!!
            .getElementsByTag("tbody")[0]
            .getElementsByTag("tr")
            .removeClass("repeated-header odd")

        val benchmarks = tables.map {
            val td = it.getElementsByTag("td")
            BenchmarkModel(
                td[0].getElementsByTag("a")[0].text(),
                td[0].getElementsByTag("a")[0].attr("href"),
                td[1].text().replace(",", "").toInt(),
                td[2].text().replace(",", "").toInt()
            )
        }

        benchmarks
    }

    companion object {
        private const val cpus_url = "https://www.cpubenchmark.net/cpu_list.php"
        private const val gpus_url = "https://www.videocardbenchmark.net/gpu_list.php"
    }
}