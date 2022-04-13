import * as React from 'react';
import { StyleSheet, View, Text, Image } from 'react-native';
import { getVideoThumbnail } from 'react-native-create-react-native-video-thumbnail';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    getVideoThumbnail(
      'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
      'BigBuckBunny'
    ).then((res) => {
      console.log('Video thumbnail', res);
      setResult(res);
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <Image
        source={{
          uri: result,
        }}
        style={{ height: 300, width: 300 }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
