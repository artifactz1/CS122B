import 'react-native-gesture-handler'
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Login from './app/src/views/screens/Login';
import Register from './app/src/views/screens/Register';

const Stack = createStackNavigator();
const App = () => {
  return (
        <NavigationContainer>
            <Stack.Navigator screenOptions={{header:()=>null}}>
                <Stack.Screen name="Register" component={Register} />
                <Stack.Screen name="Login" component={Login} />
            </Stack.Navigator>
        </NavigationContainer>
  );
}

export default App;