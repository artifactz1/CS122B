import 'react-native-gesture-handler'
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Login from './app/src/views/pages/Login';
import Register from './app/src/views/pages/Register';
import Search from './app/src/views/pages/Search';

const Stack = createStackNavigator();
const App = () => {
  return (
        <NavigationContainer>
            <Stack.Navigator screenOptions={{header:()=>null}}>
                <Stack.Screen name="Register" component={Register} />
                <Stack.Screen name="Login" component={Login} />
                <Stack.Screen name="Search" component={Search} />
            </Stack.Navigator>
        </NavigationContainer>
  );
}

export default App;