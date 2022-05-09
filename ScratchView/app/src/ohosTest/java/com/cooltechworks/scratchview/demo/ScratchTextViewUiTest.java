 /*
  * Copyright (c) 2021 Huawei Device Co., Ltd.
  * <p>
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * <p>
  * http://www.apache.org/licenses/LICENSE-2.0
  * <p>
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

 package com.cooltechworks.scratchview.demo;

 import com.cooltechworks.views.ScratchTextView;
 import ohos.aafwk.ability.Ability;
 import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
 import ohos.aafwk.ability.delegation.IAbilityDelegator;
 import ohos.agp.components.Text;
 import org.junit.After;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;

 public class ScratchTextViewUiTest {

     private Ability ability;
     private IAbilityDelegator abilityDelegator;

     @Before
     public void setUp() throws Exception {
         abilityDelegator = AbilityDelegatorRegistry.getAbilityDelegator();
         ability = EventHelper.startAbility(DemoClothingAbility.class);
         EventHelper.waitForActive(ability, 5);
         try {
             Thread.sleep(2000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }

     @After
     public void tearDown() throws Exception {
         abilityDelegator.stopAbility(ability);
         abilityDelegator = null;
         EventHelper.clearAbilities();
     }

     @Test
     public void scratchTextTest() {
         ScratchTextView scratchTextView = (ScratchTextView) ability.findComponentById(ResourceTable.Id_scratch_view);
         Text titleText = (Text) ability.findComponentById(ResourceTable.Id_scratch_title_text);
         String originContent = titleText.getText().toString();
         EventHelper.simulateMoveOnScreen(ability, scratchTextView, 300);
         try {
             Thread.sleep(2000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         String currentContent = titleText.getText().toString();
         Assert.assertNotEquals(originContent, currentContent);
     }
 }