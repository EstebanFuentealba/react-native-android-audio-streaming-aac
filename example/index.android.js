/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';

var React = require('react-native');
var RCTDeviceEventEmitter = require('RCTDeviceEventEmitter');
var TouchableHighlight = require('TouchableHighlight');
var Subscribable = require('Subscribable');
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} = React;

var AACStreamingAndroid = require('react-native-android-audio-streaming-aac');

var examplestreaming = React.createClass({
  mixins: [Subscribable.Mixin],
  getInitialState: function() {
    return {
      streaming: false,
      eventName: 'none'
    };
  },
  componentWillMount: function() {
    this.addListenerOn(RCTDeviceEventEmitter, 'streamingOpen', (e: Event) => {
      this.setState({
        streaming: true
      })
    });
    this.addListenerOn(RCTDeviceEventEmitter, 'streamingEvent', (e: Event) => {
      this.setState({
        eventName: e.eventName
      })
    });

  },
  onPressButtonPlay: function() {
    AACStreamingAndroid.play();
  },
  onPressButtonURL: function() {
    AACStreamingAndroid.setURLStreaming('http://tunein.digitalproserver.com/bioconcebb.aac');
  },
  render: function() {
    let html;
    if(!this.state.streaming) {
      html = (<TouchableHighlight onPress={this.onPressButtonURL}>
        <Text style={styles.instructions}>
          SET URL
        </Text>
      </TouchableHighlight>);
    } else {
      html = (<TouchableHighlight onPress={this.onPressButtonPlay}>
        <Text style={styles.instructions}>
          PLAY
        </Text>
      </TouchableHighlight>);
    }
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        {html}
        <Text style={styles.instructions}>
          {this.state.eventName}
        </Text>
      </View>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('example', () => examplestreaming);
