import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DriverAward } from './driver-award.model';
import { DriverAwardPopupService } from './driver-award-popup.service';
import { DriverAwardService } from './driver-award.service';

@Component({
    selector: 'jhi-driver-award-delete-dialog',
    templateUrl: './driver-award-delete-dialog.component.html'
})
export class DriverAwardDeleteDialogComponent {

    driverAward: DriverAward;

    constructor(
        private driverAwardService: DriverAwardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.driverAwardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'driverAwardListModification',
                content: 'Deleted an driverAward'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-driver-award-delete-popup',
    template: ''
})
export class DriverAwardDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private driverAwardPopupService: DriverAwardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.driverAwardPopupService
                .open(DriverAwardDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
