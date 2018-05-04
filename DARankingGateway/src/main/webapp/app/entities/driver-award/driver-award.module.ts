import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DaRankingGatewaySharedModule } from '../../shared';
import {
    DriverAwardService,
    DriverAwardPopupService,
    DriverAwardComponent,
    DriverAwardDetailComponent,
    DriverAwardDialogComponent,
    DriverAwardPopupComponent,
    DriverAwardDeletePopupComponent,
    DriverAwardDeleteDialogComponent,
    driverAwardRoute,
    driverAwardPopupRoute,
} from './';

const ENTITY_STATES = [
    ...driverAwardRoute,
    ...driverAwardPopupRoute,
];

@NgModule({
    imports: [
        DaRankingGatewaySharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        DriverAwardComponent,
        DriverAwardDetailComponent,
        DriverAwardDialogComponent,
        DriverAwardDeleteDialogComponent,
        DriverAwardPopupComponent,
        DriverAwardDeletePopupComponent,
    ],
    entryComponents: [
        DriverAwardComponent,
        DriverAwardDialogComponent,
        DriverAwardPopupComponent,
        DriverAwardDeleteDialogComponent,
        DriverAwardDeletePopupComponent,
    ],
    providers: [
        DriverAwardService,
        DriverAwardPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DaRankingGatewayDriverAwardModule {}
