import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { StatusBar } from 'expo-status-bar';
import { useEffect, useState } from 'react';
import {
    StyleSheet,
    Text,
    View,
    FlatList,
    Alert,
    Button,
    TextInput,
    ScrollView,
    TouchableOpacity,
    Platform,
    ActivityIndicator,
    ListRenderItem,
    TextStyle,
    ViewStyle
} from 'react-native';
import apiClient from './src/api/axiosConfig';
import { UserService, User } from './src/api/UserService';
import { RentalService, Rental } from './src/api/RentalService';
import { SportsFacilityService, SportsFacility } from './src/api/SportsFacilityService';


const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 10,
        backgroundColor: '#121212'
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 15
    },
    card: {
        backgroundColor: '#1e1e1e',
        padding: 16,
        marginBottom: 12,
        borderRadius: 8,
        borderWidth: 1,
        borderColor: '#333'
    },
    title: {
        color: '#90caf9',
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 8
    },
    text: {
        color: 'white',
        fontSize: 14,
        marginBottom: 4
    },
    statusActive: {
        color: '#66bb6a',
        fontWeight: 'bold'
    },
    statusBlocked: {
        color: '#ef5350',
        fontWeight: 'bold'
    },
    input: {
        backgroundColor: '#2c2c2c',
        color: 'white',
        padding: 12,
        borderRadius: 6,
        borderWidth: 1,
        borderColor: '#444',
        marginBottom: 12
    },
    inputReadonly: {
        backgroundColor: '#1a1a1a',
        color: '#aaa'
    },

    roleContainer: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        gap: 10,
        marginTop: 5,
        marginBottom: 15
    },
    roleBtn: {
        padding: 10,
        borderWidth: 1,
        borderColor: '#555',
        borderRadius: 5,
        backgroundColor: '#222'
    },
    roleBtnSelected: {
        backgroundColor: '#90caf9',
        borderColor: '#90caf9'
    },
    roleText: {
        color: 'white'
    },
    roleTextSelected: {
        color: 'black',
        fontWeight: 'bold'
    },
    marginTop: { marginTop: 10 }
});

const Stack = createNativeStackNavigator();

const showAlert = (title: string, msg: string) => {
    if (Platform.OS === 'web') window.alert(title + ": " + msg);
    else Alert.alert(title, msg);
};

function UserListScreen({ navigation }: any) {
    const [users, setUsers] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);

    const loadUsers = async () => {
        setLoading(true);
        try {
            const data = await UserService.getAll();
            setUsers(data);
        } catch (e) { showAlert("Błąd", "Brak połączenia"); }
        finally { setLoading(false); }
    };

    useEffect(() => { const sub = navigation.addListener('focus', loadUsers); return sub; }, [navigation]);

    const renderUserItem: ListRenderItem<any> = ({ item }) => {
        const isActive = item.active || item.isActive;
        return (
            <View style={styles.card}>
                <Text style={styles.title}>{item.login}</Text>
                <Text style={styles.text}>{item.firstName} {item.lastName}</Text>
                <Text style={isActive ? styles.statusActive : styles.statusBlocked}>
                    {isActive ? 'AKTYWNY' : 'ZABLOKOWANY'}
                </Text>
                <View style={styles.marginTop}>
                    <Button title="Szczegóły" onPress={() => navigation.navigate('UserDetails', { userId: item.id })} />
                </View>
            </View>
        );
    };

    return (
        <View style={styles.container}>
            <View style={styles.row}>
                <Button title="Dodaj Usera" onPress={() => navigation.navigate('UserForm', {})} />
                <Button title="Rezerwacje" color="purple" onPress={() => navigation.navigate('Rentals')} />
            </View>
            <FlatList
                data={users}
                refreshing={loading}
                onRefresh={loadUsers}
                keyExtractor={(item) => item.id}
                renderItem={renderUserItem}
            />
        </View>
    );
}

function UserFormScreen({ route, navigation }: any) {
    const { userId } = route.params || {};
    const isEdit = !!userId;
    const [form, setForm] = useState({ login: '', firstName: '', lastName: '', phone: '', role: 'CLIENT' });

    useEffect(() => {
        if (isEdit) {
            UserService.getById(userId).then(u => {
                setForm({
                    login: u.login,
                    firstName: u.firstName,
                    lastName: u.lastName,
                    phone: u.phoneNumber || '',
                    role: u.accessLevel || 'CLIENT'
                });
            });
        }
    }, [userId]);

    const save = async () => {
        try {
            const payload = {
                login: form.login, firstName: form.firstName, lastName: form.lastName,
                accessLevel: form.role,
                phoneNumber: form.role === 'CLIENT' ? form.phone : undefined
            };
            if (isEdit) await UserService.update(userId, payload);
            else await UserService.create(payload);
            navigation.goBack();
        } catch(e) { showAlert("Błąd", "Nie udało się zapisać"); }
    };

    const RoleButton = ({ val, label }: any) => (
        <TouchableOpacity
            style={[styles.roleBtn, form.role === val && styles.roleBtnSelected]}
            onPress={() => setForm({...form, role: val})}
        >
            <Text style={form.role === val ? styles.roleTextSelected : styles.roleText}>{label}</Text>
        </TouchableOpacity>
    );

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.text}>Login:</Text>
            {isEdit ?
                <View style={[styles.input, styles.inputReadonly]}><Text style={{color:'#aaa'}}>{form.login}</Text></View> :
                <TextInput style={styles.input} value={form.login} onChangeText={t => setForm({...form, login: t})} placeholderTextColor="#666" placeholder="Login"/>
            }

            <Text style={styles.text}>Imię:</Text>
            <TextInput style={styles.input} value={form.firstName} onChangeText={t => setForm({...form, firstName: t})} placeholderTextColor="#666" placeholder="Imię"/>

            <Text style={styles.text}>Nazwisko:</Text>
            <TextInput style={styles.input} value={form.lastName} onChangeText={t => setForm({...form, lastName: t})} placeholderTextColor="#666" placeholder="Nazwisko"/>

            {!isEdit && (
                <View>
                    <Text style={styles.text}>Rola:</Text>
                    {}
                    <View style={styles.roleContainer}>
                        <RoleButton val="CLIENT" label="Klient" />
                        <RoleButton val="ADMIN" label="Admin" />
                        <RoleButton val="RESOURCE_MANAGER" label="Manager" />
                    </View>
                </View>
            )}

            {form.role === 'CLIENT' && (
                <>
                    <Text style={styles.text}>Telefon:</Text>
                    <TextInput style={styles.input} value={form.phone} onChangeText={t => setForm({...form, phone: t})} keyboardType="phone-pad" placeholderTextColor="#666" placeholder="Telefon"/>
                </>
            )}
            <Button title="Zapisz" onPress={save} />
        </ScrollView>
    );
}

function UserDetailsScreen({ route, navigation }: any) {
    const { userId } = route.params;
    const [user, setUser] = useState<any>(null);
    const [rentals, setRentals] = useState<any[]>([]);

    const load = async () => {
        try {
            const u = await UserService.getById(userId);
            setUser(u);
            if (u.accessLevel === 'CLIENT' || u.phoneNumber) {
                const r = await RentalService.getByClientId(userId);
                setRentals(r);
            }
        } catch(e) {}
    };

    useEffect(() => { load(); }, []);

    const toggleActive = async () => {
        const active = user.active || user.isActive;
        try {
            if (active) await UserService.deactivate(userId);
            else await UserService.activate(userId);
            load();
        } catch(e) { showAlert("Błąd", "Błąd zmiany statusu"); }
    };

    if (!user) return <View style={styles.container}><ActivityIndicator color="#90caf9" /></View>;
    const active = user.active || user.isActive;

    return (
        <ScrollView style={styles.container}>
            <View style={styles.card}>
                <Text style={styles.title}>{user.firstName} {user.lastName}</Text>
                <Text style={styles.text}>Login: {user.login}</Text>
                <Text style={styles.text}>Status: <Text style={active ? styles.statusActive : styles.statusBlocked}>{active ? 'AKTYWNY' : 'ZABLOKOWANY'}</Text></Text>
                <View style={[styles.row, {marginTop:10}]}>
                    <Button title="Edytuj" onPress={() => navigation.navigate('UserForm', { userId })} />
                    <Button title={active ? "Zablokuj" : "Aktywuj"} color={active ? "red" : "green"} onPress={toggleActive} />
                </View>
            </View>
            <Text style={styles.title}>Rezerwacje:</Text>
            {rentals.map(r => (
                <View key={r.id} style={styles.card}>
                    <Text style={styles.text}>Obiekt ID: {r.facilityId}</Text>
                    <Text style={styles.text}>{new Date(r.beginTime).toLocaleString()}</Text>
                </View>
            ))}
            <Button title="+ Nowa Rezerwacja" color="purple" onPress={() => navigation.navigate('RentalForm', { userId })} />
        </ScrollView>
    );
}

function RentalFormScreen({ route, navigation }: any) {
    const { userId } = route.params;
    const [date, setDate] = useState('2026-06-01');
    const [startTime, setStartTime] = useState('10:00');
    const [endTime, setEndTime] = useState('12:00');
    const [facilityId, setFacilityId] = useState('');

    useEffect(() => {
        SportsFacilityService.getAll().then(data => {
            if(data.length > 0) setFacilityId(data[0].id);
        });
    }, []);

    const submit = async () => {
        try {
            await RentalService.create({
                clientId: userId, facilityId,
                startTime: `${date}T${startTime}:00`,
                endTime: `${date}T${endTime}:00`
            });
            navigation.goBack();
        } catch(e) { showAlert("Błąd", "Termin zajęty"); }
    };

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.text}>ID Obiektu:</Text>
            <TextInput style={styles.input} value={facilityId} onChangeText={setFacilityId} placeholderTextColor="#666"/>
            <Text style={styles.text}>Data (YYYY-MM-DD):</Text>
            <TextInput style={styles.input} value={date} onChangeText={setDate} placeholderTextColor="#666"/>
            <Text style={styles.text}>Start (HH:MM):</Text>
            <TextInput style={styles.input} value={startTime} onChangeText={setStartTime} placeholderTextColor="#666"/>
            <Text style={styles.text}>Koniec (HH:MM):</Text>
            <TextInput style={styles.input} value={endTime} onChangeText={setEndTime} placeholderTextColor="#666"/>
            <Button title="Zatwierdź" onPress={submit} />
        </ScrollView>
    );
}

function RentalsScreen() {
    const [list, setList] = useState<any[]>([]);
    useEffect(() => { RentalService.getAll().then(setList).catch(() => {}); }, []);

    const renderRentalItem: ListRenderItem<any> = ({ item }) => (
        <View style={styles.card}>
            <Text style={styles.text}>Obiekt: {item.facilityId}</Text>
            <Text style={styles.text}>Start: {new Date(item.beginTime).toLocaleString()}</Text>
        </View>
    );

    return (
        <View style={styles.container}>
            <FlatList
                data={list}
                keyExtractor={i => i.id}
                renderItem={renderRentalItem}
            />
        </View>
    );
}

export default function App() {
    return (
        <NavigationContainer>
            <StatusBar style="light" />
            <Stack.Navigator screenOptions={{ headerStyle: { backgroundColor: '#1e1e1e' }, headerTintColor: '#fff' }}>
                <Stack.Screen name="UserList" component={UserListScreen} />
                <Stack.Screen name="UserDetails" component={UserDetailsScreen} />
                <Stack.Screen name="UserForm" component={UserFormScreen} />
                <Stack.Screen name="RentalForm" component={RentalFormScreen} />
                <Stack.Screen name="Rentals" component={RentalsScreen} />
            </Stack.Navigator>
        </NavigationContainer>
    );
}