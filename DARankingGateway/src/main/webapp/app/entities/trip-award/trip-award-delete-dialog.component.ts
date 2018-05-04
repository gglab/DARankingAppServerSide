import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TripAward } from './trip-award.model';
import { TripAwardPopupService } from './trip-award-popup.service';
import { TripAwardService } from './trip-award.service';

@Component({
    selector: 'jhi-trip-award-delete-dialog',
    templateUrl: './trip-award-delete-dialog.component.html'
})
export class TripAwardDeleteDialogComponent {

    tripAward: TripAward;

    constructor(
        private tripAwardService: TripAwardService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.tripAwardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'tripAwardListModification',
                content: 'Deleted an tripAward'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-trip-award-delete-popup',
    template: ''
})
export class TripAwardDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tripAwardPopupService: TripAwardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.tripAwardPopupService
                .open(TripAwardDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
