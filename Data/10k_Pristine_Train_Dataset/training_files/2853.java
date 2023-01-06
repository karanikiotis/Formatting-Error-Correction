package com.polidea.rxandroidble.internal.operations;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.internal.connection.PayloadSizeLimitProvider;

import java.util.concurrent.TimeUnit;

public interface OperationsProvider {

    RxBleRadioOperationCharacteristicLongWrite provideLongWriteOperation(
            BluetoothGattCharacteristic bluetoothGattCharacteristic,
            RxBleConnection.WriteOperationAckStrategy writeOperationAckStrategy,
            PayloadSizeLimitProvider maxBatchSizeProvider,
            byte[] bytes);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    RxBleRadioOperationMtuRequest provideMtuChangeOperation(int requestedMtu);

    RxBleRadioOperationCharacteristicRead provideReadCharacteristic(BluetoothGattCharacteristic characteristic);

    RxBleRadioOperationDescriptorRead provideReadDescriptor(BluetoothGattDescriptor descriptor);

    RxBleRadioOperationReadRssi provideRssiReadOperation();

    RxBleRadioOperationServicesDiscover provideServiceDiscoveryOperation(long timeout, TimeUnit timeUnit);

    RxBleRadioOperationCharacteristicWrite provideWriteCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data);

    RxBleRadioOperationDescriptorWrite provideWriteDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] data);

    RxBleRadioOperationConnectionPriorityRequest provideConnectionPriorityChangeOperation(
            int connectionPriority,
            long delay,
            TimeUnit timeUnit
    );
}
