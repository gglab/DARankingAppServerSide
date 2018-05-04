import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { TripAward } from './trip-award.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TripAward>;

@Injectable()
export class TripAwardService {

    private resourceUrl =  SERVER_API_URL + 'api/trip-awards';

    constructor(private http: HttpClient) { }

    create(tripAward: TripAward): Observable<EntityResponseType> {
        const copy = this.convert(tripAward);
        return this.http.post<TripAward>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(tripAward: TripAward): Observable<EntityResponseType> {
        const copy = this.convert(tripAward);
        return this.http.put<TripAward>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TripAward>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TripAward[]>> {
        const options = createRequestOption(req);
        return this.http.get<TripAward[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TripAward[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TripAward = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TripAward[]>): HttpResponse<TripAward[]> {
        const jsonResponse: TripAward[] = res.body;
        const body: TripAward[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TripAward.
     */
    private convertItemFromServer(tripAward: TripAward): TripAward {
        const copy: TripAward = Object.assign({}, tripAward);
        return copy;
    }

    /**
     * Convert a TripAward to a JSON which can be sent to the server.
     */
    private convert(tripAward: TripAward): TripAward {
        const copy: TripAward = Object.assign({}, tripAward);
        return copy;
    }
}
