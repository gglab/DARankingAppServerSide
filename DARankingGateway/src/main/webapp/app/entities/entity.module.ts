import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DaRankingGatewayTripAwardModule } from './trip-award/trip-award.module';
import { DaRankingGatewayDriverAwardModule } from './driver-award/driver-award.module';
import { DaRankingGatewayTripModule } from './trip/trip.module';
import { DaRankingGatewayDriverModule } from './driver/driver.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DaRankingGatewayTripAwardModule,
        DaRankingGatewayDriverAwardModule,
        DaRankingGatewayTripModule,
        DaRankingGatewayDriverModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DaRankingGatewayEntityModule {}
