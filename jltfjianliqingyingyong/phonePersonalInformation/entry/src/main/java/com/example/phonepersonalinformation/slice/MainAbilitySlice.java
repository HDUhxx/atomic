package com.example.phonepersonalinformation.slice;

import com.example.phonepersonalinformation.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_main);

        Button basicInformation = (Button)findComponentById(ResourceTable.Id_basicInformation);
        basicInformation.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new basicInformationAbilitySlice(),new Intent());
            }
        });

        Button capacityHobby = (Button)findComponentById(ResourceTable.Id_capacityHobby);
        capacityHobby.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new capacityHobbyAbilitySlice(),new Intent());
            }
        });

        Button contact = (Button)findComponentById(ResourceTable.Id_contact);
        contact.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new contactAbilitySlice(),new Intent());
            }
        });

        Button studyCertificate = (Button)findComponentById(ResourceTable.Id_studyCertificate);
        studyCertificate.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                present(new studyCertificateAbilitySlice(),new Intent());
            }
        });


    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
