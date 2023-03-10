# HWBenchmarks

An application built to show benchmark scores of Single CPUs and GPUs.

The application uses data from [CPU Benchmark](https://www.cpubenchmark.net) and [GPU Benchmark](https://www.videocardbenchmark.net) to obtain data using [JSoup](https://jsoup.org/)

The application uses [Jetpack Compose](https://developer.android.com/jetpack/compose) for all its UI, and [Material UI](https://m3.material.io/) for design and colors.
Colors are obtained from the System using the Android 12 [Dynamic Colors API](https://developer.android.com/develop/ui/views/theming/dynamic-colors), built into the Material3 component of Jetpack Compose.

Basic dependency injection is used in the application for the Repository class, using [Dagger Hilt](https://dagger.dev/hilt/) and its appropriate [Android](https://developer.android.com/training/dependency-injection/hilt-android) and [Compose](https://developer.android.com/training/dependency-injection/hilt-jetpack#compose) dependencies

![Screenshot](https://github.com/dovahkiin98/HWBenchmarks/blob/master/screenshot/Screenshot%2001.png?raw=true "Optional title")
