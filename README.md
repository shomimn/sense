# sense

Sense is a sensing and monitoring application for Android with a wide variety of sensors (called trackers from this point on) and ways to visualize the collected data.

Implemented trackers:

1. Activity Tracker (using Google's Activity Recognition API)
2. Battery Tracker
3. Call Log Tracker
4. Camera Tracker
5. Microphone Tracker
6. Running Applications Tracker
7. SMS Tracker
8. Screen Tracker
9. Steps Tracker
10. WiFi Tracker

Implemented visualizations:

1. Text
2. Bar Chart
3. Pie Chart
4. Line Chart
5. List View
6. Map

Sense consists of 4 main activities:

### Dashboard
Used to visualize the collected data for the day. Since trackers can have multiple visualizations, each of them can be shown on the dashboard. 
Clicking on a dashboard card opens up a visualization activity for that tracker.

![Dashboard](http://i.imgur.com/cCF97Yz.png?1)

### Visualization
Shows available visualizations as tabs, and attributes which can be visualized as a dropdown. Users can choose a date range as a filter, in which case all data in that period would be visualized.

![Visualization](http://i.imgur.com/6VE8nU1.png?1) ![Visualization](http://i.imgur.com/fqmXT9k.png?1)

![Visualization](http://i.imgur.com/QIUXpRM.png?1) ![Visualization](http://i.imgur.com/HtbXKO6.png?1)

### Trackers
Trackers activity shows the available trackers and if they're turned on or off. There's also a **update interval** setting, which defines how often collected data will be sent to the remote server.
Clicking on a tracker card opens up a settings activity for the given tracker. Long click on a tracker card selects it for visualizing data on the map.

![Trackers](http://i.imgur.com/aog64qi.png?1) ![Trackers](http://i.imgur.com/5aWWmJl.png?1)

### Tracker settings
All trackers have a set of settings that can be tuned further. These settings are separated in 3 sections:

1. Sense Interval - Defines how fast new data will be collected. This setting is modifiable only for pull trackers, as push trackers collect data on change.

2. Visualizations - List of possible ways to visualize the collected data. Checking or unchecking the box defines if that visualization will be shown on the **dashboard**.

3. Sensor specific settings
- Steps tracker has a **daily goal** setting. This is basically how many steps the user wants to make during the day. Going over the goal will send a notification congratulating the user on achieving the goal.
- Activity tracker has a **daily goal** setting, showing how many minutes user wants to be active throughout the day. Going over the goal will send a notification congratulating the user on achieving the goal. 
- Screen tracker has a **daily limit** setting. If screen-on hours go above the limit, **sense** will warn the user that they should be more active throughout the day.
- Microphone tracker has a **noise threshold** setting. If noise levels surpass the threshold, user will be alerted and location in which it happened will be recorded.

![Settings](http://i.imgur.com/7bnjRke.png?1) ![Settings](http://i.imgur.com/F6CbDsq.png?1)

## Architecture

Sense is based around 4 main concepts:

1. Tracker
2. Visualization Adapter
3. Model
4. View Initializer

### Tracker

Trackers represent the main model of Sense. Each tracker is composed of a **resource**, **accent** and a **theme** which are used for grouping and visual purposes. Besides that, trackers maintain a collection of **attributes** that can be visualized, how these attributes will be visualized and which adapter will be used to adapt/transform the collected data to the expected format.


#### Merged Tracker

Merged Tracker is a special kind of tracker that’s used to fuse multiple trackers into a single one. Given a visualization type, merged tracker will collect corresponding **models** and join them into one **model** that can be visualized.


### Visualization Adapter

**Visualization Adapters** are responsible for transforming the collected data (instance of SensorData/collection of SensorData instances) into the type that the **View** is expecting. Adapters are meant to be stateless as their only concern is how to transform data from type A to type B. 

#### Aggregation

Depending on the **tracker** (more specifically how data is collected), some adapters need to aggregate the data to be able to transform it correctly. This is usually done when adapting data that's received from the server.

### Model

**Model** is a contextual wrapper around the adapted data. This means that models store additional information besides the data that needs to be displayed: e.g. which **tracker** data originated from, or which of the attributes is visualized, etc.
There’s a **model** for each of the visualization types.

### View Initializer

**View Initializers** represent stateless view controllers that are basically the “glue” between **views** and **models**. 

Each **View Initializer** goes through the next steps:
1. Construct a **view**
2. Initialize the **view** with the provided data
3. Setup Update/Clear callbacks

From that point on, **views** can be modified only through callbacks mentioned in step 3. Update/Clear callbacks are essentially closures that capture the scope in which **views** are created and initialized.

**View Initializers** are “tagged” in two ways: 
1. By **model** class type
2. By visualization type

This means that any part of the application can request the correct **view initializer** only by knowing which data should be displayed, or how it should be displayed (visualization type).

## Emotion Sense

Another big part of Sense is [Emotion Sense](http://emotionsense.github.io/index.html) library. This is an open-source library developed as a part of the EPSRC Ubhave project licensed with the [BSD License](https://en.wikipedia.org/wiki/BSD_licenses). 

Emotion sense consists of:

1. Sensor Manager

    The ESSensorManager library provides a uniform and easily configurable access to sensor data, supporting both one-off and continuous sensing scenarios.

2. Sensor Data Manager

    The ESSensorDataManager library allows you to format, store and transfer data to your server.
    
### Sensor Data

Emotion Sense defines "sensor" as any signal that can be unobtrusively captured from the smartphone device. Sense app follows the same convention and principles for adding and managing sensors data. 
In SensorManager library you can find two types of sensors:

**Pull Sensors**

All sensors that the Android OS does not capture data from until requested to do so by an application. 

**Push Sensors**

The Android OS publishes data about particular events that applications can receive, this set of sensors receives this information on behalf of an application.
    
### Adding a Sensor

To add a new sensor, you need to edit the SensorManager and SensorDataManager libraries. To implement your sensor, you'll need to:

1. Decide whether your sensor is best implemented as a push (AbstractPushSensor), pull (AbstractPullSensor), or environment (AbstractEnvironmentSensor) sensor. This will determine where your code will be implemented and what AbstractSensor type it will inherit from.
2. Implement the data structure that will store your sensor's data, in the data package. Implement a processor that creates an instance of your sensor's data in the process package.
3. Implement a default configuration for your sensor in the config package. You will also need to add an entry to getDefaultConfig() in SensorConfig.
4. The ES Sensor Manager needs to be able to find your sensor. Add an int SENSOR_TYPE and String SENSOR_NAME to SensorUtils. Add an entry for your sensor in SensorEnum.
5. Add a data formatter to convert your sensor data instance into JSON in the data manager data formatter package.
6. The data formatter needs to be able to find your formatter. Add an entry in getJSONFormatter() in DataFormatter.java.

For more information on how to use and contribute to EmotionSense library, please visit [Emotion Sense](http://emotionsense.github.io/index.html).

## Libs
[Emotion Sense](http://emotionsense.org/)

[MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
