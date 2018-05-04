import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { DriverAward } from './driver-award.model';
import { DriverAwardService } from './driver-award.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-driver-award',
    templateUrl: './driver-award.component.html'
})
export class DriverAwardComponent implements OnInit, OnDestroy {
driverAwards: DriverAward[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private driverAwardService: DriverAwardService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.driverAwardService.query().subscribe(
            (res: HttpResponse<DriverAward[]>) => {
                this.driverAwards = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDriverAwards();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: DriverAward) {
        return item.id;
    }
    registerChangeInDriverAwards() {
        this.eventSubscriber = this.eventManager.subscribe('driverAwardListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
